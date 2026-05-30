package com.concalldrift.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YahooChartResponse(YahooChart chart) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record YahooChart(List<YahooResult> result, Object error) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record YahooResult(
            YahooMeta meta,
            List<Long> timestamp,
            YahooIndicators indicators
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record YahooMeta(
            String symbol,
            Double regularMarketPrice,
            Long regularMarketVolume,
            String currency,
            String exchangeTimezoneName
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record YahooIndicators(List<YahooQuote> quote) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record YahooQuote(
            List<Double> close,
            List<Long> volume
    ) {}
}
