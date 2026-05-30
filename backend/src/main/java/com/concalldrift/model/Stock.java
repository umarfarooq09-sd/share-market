package com.concalldrift.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stocks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String symbol;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(nullable = false, length = 5)
    private String exchange;

    @Column(length = 50)
    private String sector;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL)
    private List<ConcallEvent> concallEvents;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
