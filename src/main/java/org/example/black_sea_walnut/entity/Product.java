package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@DynamicInsert
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long articleId;
    private boolean isActive;
    private Long totalCount;
    private LocalDateTime createdDate;
    private String pathToImage1;
    private String pathToImage2;
    private String pathToImage3;
    private String pathToImage4;
    private int mass;
    private int massEnergy;
    private String pathToImageDescription;
    private String pathToImagePayment;
    private String pathToImagePacking;
    private String pathToImageDelivery;
    private String priceByUnit;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "products_discounts",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id")
    )
    private Set<Discount> discounts = new HashSet<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductTranslation> productTranslations = new ArrayList<>();
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "products_tastes",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "taste_id")
    )
    private Set<Taste> tastes = new HashSet<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<HistoryPrices> historyPrices;
}
