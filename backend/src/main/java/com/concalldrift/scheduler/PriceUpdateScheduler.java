package com.concalldrift.scheduler;

import com.concalldrift.model.ConcallEvent;
import com.concalldrift.model.PriceSnapshot;
import com.concalldrift.repository.ConcallEventRepository;
import com.concalldrift.repository.PriceSnapshotRepository;
import com.concalldrift.service.YahooFinanceService;
import com.concalldrift.service.YahooFinanceService.PricePoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceUpdateScheduler {

    private final ConcallEventRepository  concallEventRepository;
    private final PriceSnapshotRepository priceSnapshotRepository;
    private final YahooFinanceService     yahooFinanceService;

    // Hours at which we capture drift snapshots
    private static final List<BigDecimal> TARGET_HOURS = List.of(
            new BigDecimal("1"),
            new BigDecimal("4"),
            new BigDecimal("24"),
            new BigDecimal("48"),
            new BigDecimal("120")
    );

    // Yahoo Finance keeps intraday (1h interval) data only for last 60 days
    private static final int MAX_INTRADAY_DAYS = 59;

    /**
     * Runs 5 seconds after startup, then every 30 minutes.
     * 1. Updates the "current" price snapshot for every tracked concall event.
     * 2. Back-fills any missing 1H/4H/1D/2D/5D historical snapshots.
     */
    @Scheduled(fixedDelay = 1_800_000, initialDelay = 5_000)
    public void run() {
        log.info("=== Price update started ===");
        List<ConcallEvent> events = concallEventRepository.findAllWithStock();
        log.info("Processing {} concall events", events.size());

        for (ConcallEvent event : events) {
            try {
                updateCurrentPrice(event);
                backFillHistoricalSnapshots(event);
                Thread.sleep(400); // gentle rate-limit buffer
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Price update interrupted");
                break;
            } catch (Exception e) {
                log.warn("Error on event {} ({}): {}", event.getId(),
                        event.getStock().getSymbol(), e.getMessage());
            }
        }
        log.info("=== Price update complete ===");
    }

    // ─── Current price (NOW DRIFT column) ────────────────────────────────────

    private void updateCurrentPrice(ConcallEvent event) {
        Optional<BigDecimal> priceOpt = yahooFinanceService.getCurrentPrice(
                event.getStock().getSymbol(), event.getStock().getExchange());

        priceOpt.ifPresent(price -> {
            BigDecimal hoursAfter = hoursElapsed(event.getAnnouncedAt(), LocalDateTime.now());
            BigDecimal drift      = calcDrift(event.getBaselinePrice(), price);

            PriceSnapshot snap = PriceSnapshot.builder()
                    .concallEvent(event)
                    .stock(event.getStock())
                    .snapshotTime(LocalDateTime.now())
                    .price(price)
                    .volume(0L)
                    .driftPct(drift)
                    .hoursAfterConcall(hoursAfter)
                    .build();
            priceSnapshotRepository.save(snap);
            log.info("Current price saved: {} → ₹{} (drift {}%)",
                    event.getStock().getSymbol(), price, drift);
        });
    }

    // ─── Historical snapshot back-fill (1H / 4H / 1D / 2D / 5D) ─────────────

    private void backFillHistoricalSnapshots(ConcallEvent event) {
        LocalDateTime announcedAt = event.getAnnouncedAt();
        boolean canUseIntraday = event.getConcallDate()
                .isAfter(LocalDate.now().minusDays(MAX_INTRADAY_DAYS));

        for (BigDecimal targetHour : TARGET_HOURS) {
            LocalDateTime targetTime = announcedAt.plusMinutes(
                    targetHour.multiply(BigDecimal.valueOf(60)).longValue());

            // Skip future snapshots
            if (targetTime.isAfter(LocalDateTime.now())) continue;

            // Skip if snapshot already exists
            BigDecimal window = targetHour.multiply(new BigDecimal("0.3"));
            if (priceSnapshotRepository.findNearestSnapshot(
                    event.getId(), targetHour,
                    targetHour.subtract(window),
                    targetHour.add(window)).isPresent()) continue;

            // Fetch from Yahoo Finance
            List<PricePoint> points = canUseIntraday
                    ? yahooFinanceService.getHourlyPrices(
                            event.getStock().getSymbol(), event.getStock().getExchange(),
                            targetTime.minusHours(3), targetTime.plusHours(3))
                    : yahooFinanceService.getDailyPrices(
                            event.getStock().getSymbol(), event.getStock().getExchange(),
                            targetTime.minusDays(1), targetTime.plusDays(1));

            if (points.isEmpty()) {
                log.debug("No Yahoo data for {}H snapshot of {}",
                        targetHour, event.getStock().getSymbol());
                continue;
            }

            // Pick the closest data point to the target time
            PricePoint closest = points.stream()
                    .min(Comparator.comparingLong(p ->
                            Math.abs(Duration.between(p.time(), targetTime).toMinutes())))
                    .orElse(null);

            if (closest == null) continue;

            BigDecimal drift = calcDrift(event.getBaselinePrice(), closest.price());
            PriceSnapshot snap = PriceSnapshot.builder()
                    .concallEvent(event)
                    .stock(event.getStock())
                    .snapshotTime(closest.time())
                    .price(closest.price())
                    .volume(closest.volume())
                    .driftPct(drift)
                    .hoursAfterConcall(targetHour)
                    .build();
            priceSnapshotRepository.save(snap);
            log.info("Saved {}H snapshot for {} → ₹{} (drift {}%)",
                    targetHour, event.getStock().getSymbol(), closest.price(), drift);
        }
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private BigDecimal calcDrift(BigDecimal baseline, BigDecimal current) {
        if (baseline == null || baseline.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return current.subtract(baseline)
                .divide(baseline, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal hoursElapsed(LocalDateTime from, LocalDateTime to) {
        long minutes = Duration.between(from, to).toMinutes();
        return BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }
}
