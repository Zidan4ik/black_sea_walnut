package org.example.black_sea_walnut.entity.translation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.LanguageCode;

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

}
