package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.ResponseProductForAdd;
import org.example.black_sea_walnut.dto.ResponseProductForView;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;


public class ProductMapper {
    public ResponseProductForView toDTOForView(Product entity, LanguageCode languageCode) {
        ProductTranslation translation = entity.getProductTranslations().stream()
                .filter(l -> l.getLanguageCode() == languageCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + languageCode));

        return ResponseProductForView
                .builder()
                .id(entity.getId())
                .name(translation.getName())
                .taste(String.valueOf(entity.getTaste().getName()))
                .totalCount(String.valueOf(entity.getTotalCount()))
                .priceByUnit(String.valueOf(entity.getPrice()))
                .discountType(entity.getDiscount().getName())
                .build();
    }

    public ResponseProductForAdd toDTOForAdd(Product entity) {
        ProductTranslation productInUk = entity.getProductTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().get();
        ProductTranslation productInEn = entity.getProductTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().get();

        return ResponseProductForAdd
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
}
