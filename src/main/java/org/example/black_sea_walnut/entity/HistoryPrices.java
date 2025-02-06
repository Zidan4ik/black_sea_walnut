package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "histories_prices")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoryPrices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int newPrice;
    private int currentPrice;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public HistoryPrices(int currentPrice, LocalDateTime validFrom, LocalDateTime validTo, Product product) {
        this.currentPrice = currentPrice;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.product = product;
    }
}
