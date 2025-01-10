package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;

import java.time.LocalDateTime;
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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "products_discounts",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "articleId"),
            inverseJoinColumns = @JoinColumn(name = "discount_id", referencedColumnName = "discountId")
    )
    private Set<Discount> discounts;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductTranslation> productTranslations;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "products_tastes",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "articleId"),
            inverseJoinColumns = @JoinColumn(name = "taste_id", referencedColumnName = "tasteId")
    )
    private Set<Taste> tastes;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoryPrices> historyPrices;
}
