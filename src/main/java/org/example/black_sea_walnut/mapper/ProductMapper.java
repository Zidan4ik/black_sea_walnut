package org.example.black_sea_walnut.mapper;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.discount.ResponseDiscountForView;
import org.example.black_sea_walnut.dto.product.ProductRequestForAdd;
import org.example.black_sea_walnut.dto.product.ProductResponseForAdd;
import org.example.black_sea_walnut.dto.product.ResponseAllDiscountsAndTastes;
import org.example.black_sea_walnut.dto.product.ResponseProductForView;
import org.example.black_sea_walnut.dto.taste.ResponseTasteForView;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final HistoryPricesMapper historyPricesMapper;

    public ResponseProductForView toDTOForView(Product entity, LanguageCode languageCode) {
        ProductTranslation translation = entity.getProductTranslations().stream()
                .filter(l -> l.getLanguageCode() == languageCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + languageCode));
        return null;
//        return ResponseProductForView
//                .builder()
//                .id(entity.getId())
//                .name(translation.getName())
//                .taste(String.valueOf(entity.getTaste().getName()))
//                .totalCount(String.valueOf(entity.getTotalCount()))
//                .priceByUnit(String.valueOf(entity.getPrice()))
//                .discountType(entity.getDiscount().getName())
//                .build();
    }

    public ProductResponseForAdd toResponseForAdd(Product entity) {
        ProductTranslation productInUk = entity.getProductTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().get();
        ProductTranslation productInEn = entity.getProductTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().get();

        return ProductResponseForAdd
                .builder()
                .id(entity.getId())
                .articleId(entity.getArticleId())
                .amount(String.valueOf(entity.getTotalCount()))
                .mass(String.valueOf(entity.getMass()))
                .energyMass(String.valueOf(entity.getMassEnergy()))
                .nameUk(productInUk.getName())
                .conditionExploitationUk(productInUk.getConditionExploitation())
                .descriptionProductUk(productInUk.getDescriptionProduct())
                .descriptionPackingUk(productInUk.getDescriptionPacking())
                .descriptionPaymentUk(productInUk.getDescriptionPayment())
                .descriptionDeliveryUk(productInUk.getDescriptionDelivery())
                .nameEn(productInEn.getName())
                .descriptionProductEn(productInEn.getDescriptionProduct())
                .conditionExploitationEn(productInEn.getConditionExploitation())
                .descriptionPackingEn(productInEn.getDescriptionPacking())
                .descriptionPaymentEn(productInEn.getDescriptionPayment())
                .descriptionDeliveryEn(productInEn.getDescriptionDelivery())
                .build();
    }

    public Product toEntityForRequestAdd(ProductRequestForAdd dto) {
        Product entity = new Product();
        entity.setId(dto.getId());
        entity.setArticleId(dto.getArticleId());
        entity.setActive(dto.getIsActive());
        entity.setTotalCount(Long.valueOf(dto.getAmount()));
        ProductTranslation productUk = new ProductTranslation(LanguageCode.uk,
                dto.getNameUk(), dto.getRecipeUk(), dto.getConditionExploitationUk(), dto.getDescriptionProductUk(),
                dto.getDescriptionPackingUk(), dto.getDescriptionPaymentUk(), dto.getDescriptionDeliveryUk());
        ProductTranslation productEn = new ProductTranslation(LanguageCode.en,
                dto.getNameEn(), dto.getRecipeEn(), dto.getConditionExploitationEn(), dto.getDescriptionProductEn(),
                dto.getDescriptionPackingEn(), dto.getDescriptionPaymentEn(), dto.getDescriptionDeliveryEn());
        entity.setProductTranslations(List.of(productUk, productEn));
        entity.setCreatedDate(LocalDateTime.now());
        entity.setMass(dto.getMass().intValue());
        entity.setMassEnergy(dto.getEnergyMass().intValue());
        return entity;
    }
}
