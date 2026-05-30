package com.concalldrift.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "concall_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcallEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "concall_date", nullable = false)
    private LocalDate concallDate;

    @Column(name = "announced_at", nullable = false)
    private LocalDateTime announcedAt;

    @Column(name = "baseline_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal baselinePrice;

    @Column(length = 10)
    private String quarter;

    @Column(name = "fiscal_year", length = 10)
    private String fiscalYear;

    @Column(name = "result_type", length = 20)
    private String resultType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "concallEvent", cascade = CascadeType.ALL)
    private List<PriceSnapshot> snapshots;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
