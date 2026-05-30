package com.concalldrift.repository;

import com.concalldrift.model.ConcallEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ConcallEventRepository extends JpaRepository<ConcallEvent, Long> {

    List<ConcallEvent> findByStock_Id(Long stockId);

    @Query("SELECT c FROM ConcallEvent c JOIN FETCH c.stock s WHERE " +
           "s.exchange = COALESCE(:exchange, s.exchange) AND " +
           "c.concallDate >= COALESCE(:fromDate, c.concallDate) AND " +
           "c.concallDate <= COALESCE(:toDate, c.concallDate) " +
           "ORDER BY c.concallDate DESC")
    List<ConcallEvent> findWithFilters(
            @Param("exchange") String exchange,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query("SELECT DISTINCT c FROM ConcallEvent c JOIN FETCH c.stock ORDER BY c.concallDate DESC")
    List<ConcallEvent> findAllWithStock();

    @Query("SELECT DISTINCT c FROM ConcallEvent c JOIN FETCH c.stock WHERE c.concallDate >= :from ORDER BY c.concallDate DESC")
    List<ConcallEvent> findRecentEvents(@Param("from") LocalDate from);
}
