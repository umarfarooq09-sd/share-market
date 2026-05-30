package com.concalldrift.controller;

import com.concalldrift.dto.ApiResponse;
import com.concalldrift.dto.StockDto;
import com.concalldrift.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockDto>>> getAllStocks(
            @RequestParam(required = false) String exchange) {
        List<StockDto> stocks = exchange != null
                ? stockService.getStocksByExchange(exchange)
                : stockService.getAllStocks();
        return ResponseEntity.ok(ApiResponse.ok(stocks));
    }
}
