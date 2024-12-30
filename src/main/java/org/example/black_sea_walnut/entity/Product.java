package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long articleId;
    private int price;
    private Long totalCount;
    private LocalDateTime createdDate;
    private String pathToImage1;
    private String pathToImage2;
    private String pathToImage3;
    private int mass;
    private int massEnergy;
    private String pathToImageDescription;
    private String pathToImagePayment;
    private String pathToImagePacking;
    private String pathToImageDelivery;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "discount_id", referencedColumnName = "discountId")
    private Discount discount;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProductTranslation> productTranslations;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "taste_id", referencedColumnName = "tasteId")
    private Taste taste;
}
