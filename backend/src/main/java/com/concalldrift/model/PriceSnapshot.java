package com.concalldrift.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_snapshots")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concall_event_id", nullable = false)
    private ConcallEvent concallEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "snapshot_time", nullable = false)
    private LocalDateTime snapshotTime;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Long volume;

    @Column(name = "drift_pct", precision = 8, scale = 4)
    private BigDecimal driftPct;

    @Column(name = "hours_after_concall", precision = 8, scale = 2)
    private BigDecimal hoursAfterConcall;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
