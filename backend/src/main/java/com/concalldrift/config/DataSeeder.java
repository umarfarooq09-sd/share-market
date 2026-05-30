package com.concalldrift.config;

import com.concalldrift.model.ConcallEvent;
import com.concalldrift.model.PriceSnapshot;
import com.concalldrift.model.Stock;
import com.concalldrift.repository.ConcallEventRepository;
import com.concalldrift.repository.PriceSnapshotRepository;
import com.concalldrift.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final StockRepository        stockRepository;
    private final ConcallEventRepository concallEventRepository;
    private final PriceSnapshotRepository priceSnapshotRepository;

    @Override
    public void run(String... args) {
        seedStocks();
        seedConcallEvents();
        seedPriceSnapshots();
        log.info("Data seeding complete");
    }

    // ─── Stocks ──────────────────────────────────────────────────────────────

    private void seedStocks() {
        List<Object[]> stocks = List.of(
            new Object[]{"RELIANCE",   "Reliance Industries Ltd",        "NSE", "Energy"},
            new Object[]{"TCS",        "Tata Consultancy Services Ltd",  "NSE", "IT"},
            new Object[]{"HDFCBANK",   "HDFC Bank Ltd",                  "NSE", "Banking"},
            new Object[]{"INFY",       "Infosys Ltd",                    "NSE", "IT"},
            new Object[]{"ICICIBANK",  "ICICI Bank Ltd",                 "NSE", "Banking"},
            new Object[]{"BHARTIARTL", "Bharti Airtel Ltd",              "NSE", "Telecom"},
            new Object[]{"WIPRO",      "Wipro Ltd",                      "NSE", "IT"},
            new Object[]{"SBIN",       "State Bank of India",            "NSE", "Banking"},
            new Object[]{"LT",         "Larsen & Toubro Ltd",            "NSE", "Infrastructure"},
            new Object[]{"HCLTECH",    "HCL Technologies Ltd",           "NSE", "IT"},
            new Object[]{"TATAMOTORS", "Tata Motors Ltd",                "BSE", "Auto"},
            new Object[]{"ADANIENT",   "Adani Enterprises Ltd",          "NSE", "Conglomerate"},
            new Object[]{"MARUTI",     "Maruti Suzuki India Ltd",        "BSE", "Auto"},
            new Object[]{"SUNPHARMA",  "Sun Pharmaceutical Industries",  "NSE", "Pharma"},
            new Object[]{"BAJFINANCE", "Bajaj Finance Ltd",              "NSE", "NBFC"},
            new Object[]{"KOTAKBANK",  "Kotak Mahindra Bank Ltd",        "NSE", "Banking"},
            new Object[]{"TITAN",      "Titan Company Ltd",              "BSE", "Consumer"},
            new Object[]{"NESTLEIND",  "Nestle India Ltd",               "NSE", "FMCG"},
            new Object[]{"POWERGRID",  "Power Grid Corporation",         "NSE", "Utilities"},
            new Object[]{"ONGC",       "Oil and Natural Gas Corp",       "NSE", "Energy"}
        );

        for (Object[] s : stocks) {
            String symbol = (String) s[0];
            if (stockRepository.findBySymbol(symbol).isEmpty()) {
                stockRepository.save(Stock.builder()
                    .symbol(symbol)
                    .companyName((String) s[1])
                    .exchange((String) s[2])
                    .sector((String) s[3])
                    .build());
            }
        }
        log.info("Stocks seeded");
    }

    // ─── Concall Events ───────────────────────────────────────────────────────

    private record EventSeed(String symbol, String date, String announcedAt, double baseline) {}

    private void seedConcallEvents() {
        List<EventSeed> events = List.of(
            new EventSeed("RELIANCE",   "2025-04-18", "2025-04-18T14:30:00", 2850.75),
            new EventSeed("TCS",        "2025-04-11", "2025-04-11T15:00:00", 3620.40),
            new EventSeed("HDFCBANK",   "2025-04-19", "2025-04-19T14:00:00", 1748.20),
            new EventSeed("INFY",       "2025-04-17", "2025-04-17T15:30:00", 1485.60),
            new EventSeed("ICICIBANK",  "2025-04-26", "2025-04-26T14:30:00", 1292.80),
            new EventSeed("BHARTIARTL", "2025-04-30", "2025-04-30T14:00:00", 1876.50),
            new EventSeed("WIPRO",      "2025-04-16", "2025-04-16T15:00:00",  462.30),
            new EventSeed("SBIN",       "2025-05-03", "2025-05-03T14:30:00",  812.40),
            new EventSeed("LT",         "2025-05-07", "2025-05-07T14:00:00", 3485.20),
            new EventSeed("HCLTECH",    "2025-04-24", "2025-04-24T15:30:00", 1648.90),
            new EventSeed("TATAMOTORS", "2025-05-09", "2025-05-09T14:30:00",  986.40),
            new EventSeed("BAJFINANCE", "2025-05-06", "2025-05-06T15:00:00", 2784.60)
        );

        for (EventSeed e : events) {
            Optional<Stock> stockOpt = stockRepository.findBySymbol(e.symbol());
            if (stockOpt.isEmpty()) continue;
            Stock stock = stockOpt.get();
            LocalDate concallDate = LocalDate.parse(e.date());
            boolean exists = !concallEventRepository.findByStock_Id(stock.getId()).isEmpty();
            if (!exists) {
                concallEventRepository.save(ConcallEvent.builder()
                    .stock(stock)
                    .concallDate(concallDate)
                    .announcedAt(LocalDateTime.parse(e.announcedAt()))
                    .baselinePrice(BigDecimal.valueOf(e.baseline()))
                    .quarter("Q4")
                    .fiscalYear("FY2025")
                    .resultType("Quarterly")
                    .build());
            }
        }
        log.info("Concall events seeded");
    }

    // ─── Price Snapshots ──────────────────────────────────────────────────────

    private record SnapSeed(double hours, String time, double price, long volume) {}

    private void seedPriceSnapshots() {
        seedSnapsForStock("RELIANCE", "2025-04-18", List.of(
            new SnapSeed(1,   "2025-04-18T15:30:00", 2858.20, 12540000),
            new SnapSeed(4,   "2025-04-18T18:30:00", 2876.40,  8920000),
            new SnapSeed(24,  "2025-04-19T14:30:00", 2912.60, 15680000),
            new SnapSeed(48,  "2025-04-22T14:30:00", 2948.30, 18240000),
            new SnapSeed(120, "2025-04-25T14:30:00", 2994.80, 14560000)
        ));
        seedSnapsForStock("TCS", "2025-04-11", List.of(
            new SnapSeed(1,   "2025-04-11T16:00:00", 3598.40, 4280000),
            new SnapSeed(4,   "2025-04-11T19:00:00", 3572.80, 3140000),
            new SnapSeed(24,  "2025-04-14T15:00:00", 3548.60, 5920000),
            new SnapSeed(48,  "2025-04-15T15:00:00", 3524.20, 6840000),
            new SnapSeed(120, "2025-04-18T15:00:00", 3680.50, 7960000)
        ));
        seedSnapsForStock("HDFCBANK", "2025-04-19", List.of(
            new SnapSeed(1,   "2025-04-19T15:00:00", 1762.40, 18420000),
            new SnapSeed(4,   "2025-04-19T18:00:00", 1780.60, 12840000),
            new SnapSeed(24,  "2025-04-22T14:00:00", 1798.40, 22640000),
            new SnapSeed(48,  "2025-04-23T14:00:00", 1812.80, 19840000),
            new SnapSeed(120, "2025-04-26T14:00:00", 1840.20, 16920000)
        ));
        seedSnapsForStock("INFY", "2025-04-17", List.of(
            new SnapSeed(1,   "2025-04-17T16:30:00", 1472.30,  9840000),
            new SnapSeed(4,   "2025-04-17T19:30:00", 1461.80,  7240000),
            new SnapSeed(24,  "2025-04-18T15:30:00", 1498.40, 12640000),
            new SnapSeed(48,  "2025-04-21T15:30:00", 1518.90, 10940000),
            new SnapSeed(120, "2025-04-24T15:30:00", 1548.20,  8960000)
        ));
        seedSnapsForStock("ICICIBANK", "2025-04-26", List.of(
            new SnapSeed(1,   "2025-04-26T15:30:00", 1308.40, 24680000),
            new SnapSeed(4,   "2025-04-26T18:30:00", 1324.60, 18240000),
            new SnapSeed(24,  "2025-04-29T14:30:00", 1348.20, 28640000),
            new SnapSeed(48,  "2025-04-30T14:30:00", 1362.80, 22480000),
            new SnapSeed(120, "2025-05-03T14:30:00", 1388.40, 19640000)
        ));
    }

    private void seedSnapsForStock(String symbol, String concallDateStr, List<SnapSeed> snaps) {
        Optional<Stock> stockOpt = stockRepository.findBySymbol(symbol);
        if (stockOpt.isEmpty()) return;
        Stock stock = stockOpt.get();

        List<ConcallEvent> events = concallEventRepository.findByStock_Id(stock.getId());
        if (events.isEmpty()) return;
        ConcallEvent event = events.get(0);

        boolean hasSnaps = !priceSnapshotRepository
            .findByConcallEvent_IdOrderBySnapshotTimeAsc(event.getId()).isEmpty();
        if (hasSnaps) return;

        BigDecimal baseline = event.getBaselinePrice();
        for (SnapSeed s : snaps) {
            BigDecimal price = BigDecimal.valueOf(s.price());
            BigDecimal drift = price.subtract(baseline)
                .divide(baseline, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, java.math.RoundingMode.HALF_UP);

            priceSnapshotRepository.save(PriceSnapshot.builder()
                .concallEvent(event)
                .stock(stock)
                .snapshotTime(LocalDateTime.parse(s.time()))
                .price(price)
                .volume(s.volume())
                .driftPct(drift)
                .hoursAfterConcall(BigDecimal.valueOf(s.hours()))
                .build());
        }
    }
}
