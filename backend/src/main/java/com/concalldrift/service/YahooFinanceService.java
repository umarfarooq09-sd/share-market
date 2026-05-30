package com.concalldrift.service;

import com.concalldrift.dto.YahooChartResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class YahooFinanceService {

    private static final String BASE_URL = "https://query1.finance.yahoo.com/v8/finance/chart/";
    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final RestTemplate restTemplate;

    public record PricePoint(LocalDateTime time, BigDecimal price, long volume) {}

    /**
     * Fetch the current market price for a stock.
     * NSE stocks → symbol.NS  |  BSE stocks → symbol.BO
     */
    public Optional<BigDecimal> getCurrentPrice(String symbol, String exchange) {
        String yahooSymbol = toYahooSymbol(symbol, exchange);
        String url = BASE_URL + yahooSymbol + "?interval=1m&range=1d";
        try {
            YahooChartResponse resp = restTemplate.getForObject(url, YahooChartResponse.class);
            return extractMeta(resp).map(m -> BigDecimal.valueOf(m.regularMarketPrice()));
        } catch (Exception e) {
            log.warn("getCurrentPrice failed for {}: {}", yahooSymbol, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Fetch hourly OHLCV data between two timestamps.
     * Yahoo Finance keeps 60-day intraday history; for older dates use getDailyPrices.
     */
    public List<PricePoint> getHourlyPrices(String symbol, String exchange,
                                              LocalDateTime from, LocalDateTime to) {
        return fetchPrices(symbol, exchange, from, to, "1h");
    }

    /**
     * Fetch daily OHLCV data between two dates — works for any historical date.
     */
    public List<PricePoint> getDailyPrices(String symbol, String exchange,
                                             LocalDateTime from, LocalDateTime to) {
        return fetchPrices(symbol, exchange, from, to, "1d");
    }

    private List<PricePoint> fetchPrices(String symbol, String exchange,
                                          LocalDateTime from, LocalDateTime to,
                                          String interval) {
        String yahooSymbol = toYahooSymbol(symbol, exchange);
        long p1 = from.atZone(IST).toEpochSecond();
        long p2 = to.atZone(IST).toEpochSecond();
        String url = BASE_URL + yahooSymbol
                + "?interval=" + interval
                + "&period1=" + p1
                + "&period2=" + p2;
        try {
            YahooChartResponse resp = restTemplate.getForObject(url, YahooChartResponse.class);
            return parseTimeSeries(resp);
        } catch (Exception e) {
            log.warn("fetchPrices({}) failed for {}: {}", interval, yahooSymbol, e.getMessage());
            return List.of();
        }
    }

    // ─── Parsing helpers ─────────────────────────────────────────────────────

    private Optional<YahooChartResponse.YahooMeta> extractMeta(YahooChartResponse resp) {
        if (resp == null || resp.chart() == null) return Optional.empty();
        var results = resp.chart().result();
        if (results == null || results.isEmpty()) return Optional.empty();
        var meta = results.get(0).meta();
        if (meta == null || meta.regularMarketPrice() == null) return Optional.empty();
        return Optional.of(meta);
    }

    private List<PricePoint> parseTimeSeries(YahooChartResponse resp) {
        if (resp == null || resp.chart() == null) return List.of();
        var results = resp.chart().result();
        if (results == null || results.isEmpty()) return List.of();

        var result = results.get(0);
        var timestamps = result.timestamp();
        var indicators = result.indicators();
        if (timestamps == null || indicators == null) return List.of();

        var quotes = indicators.quote();
        if (quotes == null || quotes.isEmpty()) return List.of();

        var quote = quotes.get(0);
        var closes  = quote.close();
        var volumes = quote.volume();
        if (closes == null) return List.of();

        List<PricePoint> points = new ArrayList<>();
        for (int i = 0; i < timestamps.size(); i++) {
            if (i >= closes.size() || closes.get(i) == null) continue;
            BigDecimal price = BigDecimal.valueOf(closes.get(i));
            long vol = (volumes != null && i < volumes.size() && volumes.get(i) != null)
                    ? volumes.get(i) : 0L;
            LocalDateTime time = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(timestamps.get(i)), IST);
            points.add(new PricePoint(time, price, vol));
        }
        return points;
    }

    private String toYahooSymbol(String symbol, String exchange) {
        return symbol + (exchange.equalsIgnoreCase("NSE") ? ".NS" : ".BO");
    }
}
