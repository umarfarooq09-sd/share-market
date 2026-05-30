package com.concalldrift.repository;

import com.concalldrift.model.PriceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

    List<PriceSnapshot> findByConcallEvent_IdOrderBySnapshotTimeAsc(Long concallEventId);

    @Query("SELECT p FROM PriceSnapshot p WHERE p.concallEvent.id = :eventId " +
           "AND p.hoursAfterConcall BETWEEN :minH AND :maxH " +
           "ORDER BY ABS(p.hoursAfterConcall - :targetH) ASC LIMIT 1")
    Optional<PriceSnapshot> findNearestSnapshot(
            @Param("eventId") Long eventId,
            @Param("targetH") BigDecimal targetHours,
            @Param("minH") BigDecimal minH,
            @Param("maxH") BigDecimal maxH
    );

    @Query("SELECT p FROM PriceSnapshot p WHERE p.concallEvent.id = :eventId " +
           "ORDER BY p.snapshotTime DESC LIMIT 1")
    Optional<PriceSnapshot> findLatestSnapshot(@Param("eventId") Long eventId);
}
