package com.concalldrift.service;

import com.concalldrift.dto.DriftBoardRow;
import com.concalldrift.model.ConcallEvent;
import com.concalldrift.model.PriceSnapshot;
import com.concalldrift.repository.ConcallEventRepository;
import com.concalldrift.repository.PriceSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriftService {

    private final ConcallEventRepository concallEventRepository;
    private final PriceSnapshotRepository priceSnapshotRepository;

    public List<DriftBoardRow> getDriftBoard(String exchange, LocalDate fromDate, LocalDate toDate) {
        String exch = (exchange != null && !exchange.isBlank()) ? exchange : null;

        // Load all events then filter in Java — avoids any JPQL null-parameter issues
        return concallEventRepository.findAllWithStock().stream()
                .filter(e -> exch == null || exch.equalsIgnoreCase(e.getStock().getExchange()))
                .filter(e -> fromDate == null || !e.getConcallDate().isBefore(fromDate))
                .filter(e -> toDate == null || !e.getConcallDate().isAfter(toDate))
                .map(this::buildDriftRow)
                .sorted((a, b) -> b.getConcallDate().compareTo(a.getConcallDate()))
                .collect(Collectors.toList());
    }

    private DriftBoardRow buildDriftRow(ConcallEvent event) {
        Optional<PriceSnapshot> latest = priceSnapshotRepository.findLatestSnapshot(event.getId());
        BigDecimal currentPrice = latest.map(PriceSnapshot::getPrice).orElse(event.getBaselinePrice());
        Long currentVolume = latest.map(PriceSnapshot::getVolume).orElse(0L);

        return DriftBoardRow.builder()
                .concallEventId(event.getId())
                .stockId(event.getStock().getId())
                .symbol(event.getStock().getSymbol())
                .companyName(event.getStock().getCompanyName())
                .exchange(event.getStock().getExchange())
                .sector(event.getStock().getSector())
                .quarter(event.getQuarter())
                .fiscalYear(event.getFiscalYear())
                .resultType(event.getResultType())
                .concallDate(event.getConcallDate())
                .announcedAt(event.getAnnouncedAt())
                .baselinePrice(event.getBaselinePrice())
                .currentPrice(currentPrice)
                .currentVolume(currentVolume)
                .drift1h(getDriftAtHour(event.getId(), event.getBaselinePrice(), new BigDecimal("1")))
                .drift4h(getDriftAtHour(event.getId(), event.getBaselinePrice(), new BigDecimal("4")))
                .drift1d(getDriftAtHour(event.getId(), event.getBaselinePrice(), new BigDecimal("24")))
                .drift2d(getDriftAtHour(event.getId(), event.getBaselinePrice(), new BigDecimal("48")))
                .drift5d(getDriftAtHour(event.getId(), event.getBaselinePrice(), new BigDecimal("120")))
                .driftCurrent(calculateDrift(event.getBaselinePrice(), currentPrice))
                .lastUpdated(latest.map(PriceSnapshot::getSnapshotTime).orElse(event.getAnnouncedAt()))
                .build();
    }

    private BigDecimal getDriftAtHour(Long eventId, BigDecimal baseline, BigDecimal targetHours) {
        BigDecimal window = targetHours.multiply(new BigDecimal("0.25"));
        Optional<PriceSnapshot> snap = priceSnapshotRepository.findNearestSnapshot(
                eventId, targetHours,
                targetHours.subtract(window),
                targetHours.add(window)
        );
        return snap.map(s -> calculateDrift(baseline, s.getPrice())).orElse(null);
    }

    private BigDecimal calculateDrift(BigDecimal baseline, BigDecimal current) {
        if (baseline == null || baseline.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return current.subtract(baseline)
                .divide(baseline, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
