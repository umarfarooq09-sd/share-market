package com.concalldrift.repository;

import com.concalldrift.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findBySymbol(String symbol);
    List<Stock> findByExchange(String exchange);
    List<Stock> findBySector(String sector);
}
