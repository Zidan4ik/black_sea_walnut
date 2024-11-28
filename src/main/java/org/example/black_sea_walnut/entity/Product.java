package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;
import org.example.black_sea_walnut.enums.PackingType;

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
    private PackingType packingType;
    private int price;
    private LocalDateTime createdDate;
    private String pathToImage1;
    private String pathToImage2;
    private String pathToImage3;
    private int massEnergy;
    private String pathToImageDescription;
    private String pathToImagePayment;
    private String pathToImagePacking;
    private String pathToImageDelivery;
    @OneToOne
    private Mass mass;
    @OneToOne
    private Discount discount;
    @ManyToMany
    private List<Mass> masses;
    @ManyToMany
    private List<ProductTranslation> productTranslations;
}
