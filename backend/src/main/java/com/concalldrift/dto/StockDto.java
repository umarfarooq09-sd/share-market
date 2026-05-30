package com.concalldrift.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockDto {
    private Long id;
    private String symbol;
    private String companyName;
    private String exchange;
    private String sector;
}
