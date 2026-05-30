package com.concalldrift.service;

import com.concalldrift.dto.StockDto;
import com.concalldrift.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<StockDto> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(s -> StockDto.builder()
                        .id(s.getId())
                        .symbol(s.getSymbol())
                        .companyName(s.getCompanyName())
                        .exchange(s.getExchange())
                        .sector(s.getSector())
                        .build())
                .collect(Collectors.toList());
    }

    public List<StockDto> getStocksByExchange(String exchange) {
        return stockRepository.findByExchange(exchange).stream()
                .map(s -> StockDto.builder()
                        .id(s.getId())
                        .symbol(s.getSymbol())
                        .companyName(s.getCompanyName())
                        .exchange(s.getExchange())
                        .sector(s.getSector())
                        .build())
                .collect(Collectors.toList());
    }
}
