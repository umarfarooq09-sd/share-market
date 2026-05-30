package com.concalldrift.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class DriftBoardRow {
    private Long concallEventId;
    private Long stockId;
    private String symbol;
    private String companyName;
    private String exchange;
    private String sector;
    private String quarter;
    private String fiscalYear;
    private String resultType;
    private LocalDate concallDate;
    private LocalDateTime announcedAt;
    private BigDecimal baselinePrice;
    private BigDecimal currentPrice;
    private Long currentVolume;
    private BigDecimal drift1h;
    private BigDecimal drift4h;
    private BigDecimal drift1d;
    private BigDecimal drift2d;
    private BigDecimal drift5d;
    private BigDecimal driftCurrent;
    private LocalDateTime lastUpdated;
}
