package org.example.black_sea_walnut.entity.translation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;

@Entity
@Table(name = "products_translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
    private String name;
    @Lob
    private String recipe;
    @Lob
    private String conditionExploitation;
    @Lob
    private String descriptionProduct;
    @Lob
    private String descriptionPacking;
    @Lob
    private String descriptionPayment;
    @Lob
    private String descriptionDelivery;
    @ManyToOne
    private Product product;
    public ProductTranslation(LanguageCode languageCode, String name, String recipe, String conditionExploitation, String descriptionProduct, String descriptionPacking, String descriptionPayment, String descriptionDelivery, Product product) {
        this.languageCode = languageCode;
        this.name = name;
        this.recipe = recipe;
        this.conditionExploitation = conditionExploitation;
        this.descriptionProduct = descriptionProduct;
        this.descriptionPacking = descriptionPacking;
        this.descriptionPayment = descriptionPayment;
        this.descriptionDelivery = descriptionDelivery;
        this.product = product;
    }
}
