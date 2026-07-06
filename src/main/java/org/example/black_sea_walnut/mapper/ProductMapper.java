package org.example.black_sea_walnut.mapper;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.product.ProductRequestForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForViewInProducts;
import org.example.black_sea_walnut.dto.web.ProductResponseForView;
import org.example.black_sea_walnut.dto.web.ProductResponseForViewInTable;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ProductMapper implements GenericsMapper {
    public ProductResponseForViewInProducts toDTOForView(Product entity, LanguageCode languageCode) {
        ProductTranslation translation = entity.getProductTranslations().stream()
                .filter(l -> l.getLanguageCode() == languageCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + languageCode));
        Taste taste = entity.getTastes().stream().filter(t -> t.getLanguageCode().equals(languageCode))
                .findFirst().orElse(null);
        Discount discount = entity.getDiscounts().stream().filter(d -> d.getLanguageCode().equals(languageCode))
                .findFirst().orElse(null);
        String tasteName = taste != null ? taste.getName() : null;
        String discountName = discount != null ? discount.getName() : null;
        return ProductResponseForViewInProducts
                .builder()
                .id(entity.getId())
                .name(translation.getName())
                .taste(tasteName)
                .totalCount(String.valueOf(entity.getTotalCount()))
                .priceByUnit(entity.getPriceByUnit())
                .discount(discountName)
                .build();
    }

    public ProductResponseForAdd toResponseForAdd(Product entity) {
        ProductTranslation translationUk = getTranslation(entity, LanguageCode.uk);
        ProductTranslation translationEn = getTranslation(entity, LanguageCode.en);

        ProductResponseForAdd dto = ProductResponseForAdd.builder()
                .id(entity.getId())
                .articleId(entity.getArticleId())
                .isActive(entity.isActive())
                .amount(String.valueOf(entity.getTotalCount()))
                .mass(String.valueOf(entity.getMass()))
                .energyMass(String.valueOf(entity.getMassEnergy()))

                .nameUk(translationUk.getName())
                .recipeUk(translationUk.getRecipe())
                .conditionExploitationUk(translationUk.getConditionExploitation())
                .descriptionProductUk(translationUk.getDescriptionProduct())
                .descriptionPackingUk(translationUk.getDescriptionPacking())
                .descriptionPaymentUk(translationUk.getDescriptionPayment())
                .descriptionDeliveryUk(translationUk.getDescriptionDelivery())

                .nameEn(translationEn.getName())
                .recipeEn(translationEn.getRecipe())
                .descriptionProductEn(translationEn.getDescriptionProduct())
                .conditionExploitationEn(translationEn.getConditionExploitation())
                .descriptionPackingEn(translationEn.getDescriptionPacking())
                .descriptionPaymentEn(translationEn.getDescriptionPayment())
                .descriptionDeliveryEn(translationEn.getDescriptionDelivery())

                .pathToImage1(entity.getPathToImage1())
                .pathToImage2(entity.getPathToImage2())
                .pathToImage3(entity.getPathToImage3())
                .pathToImage4(entity.getPathToImage4())
                .pathToImageDescription(entity.getPathToImageDescription())
                .pathToImagePacking(entity.getPathToImagePacking())
                .pathToImagePayment(entity.getPathToImagePayment())
                .pathToImageDelivery(entity.getPathToImageDelivery())
                .build();

        entity.getTastes().stream().findFirst()
                .ifPresent(t -> dto.setTasteId(t.getCommonId()));
        entity.getDiscounts().stream().findFirst()
                .ifPresent(d -> dto.setDiscountId(d.getDiscountCommonId()));

        return dto;
    }

    public Product toEntityForRequestAdd(ProductRequestForAdd dto, Product entity) {
        entity.setId(dto.getId());
        entity.setArticleId(dto.getArticleId());
        entity.setActive(dto.getIsActive());
        entity.setTotalCount(dto.getAmount());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setMass(dto.getMass().intValue());
        entity.setMassEnergy(dto.getEnergyMass().intValue());
        if(dto.getNewPrice()!=null){
            entity.setPriceByUnit(String.valueOf(dto.getNewPrice()));
            entity.getHistoryPrices().add(new HistoryPrices(dto.getNewPrice(),
                    LocalDateTime.now(), LocalDateTime.now().plusDays(10), entity));
        }
        updateBasicFields(entity, dto);
        return entity;
    }

    public ProductResponseForViewInTable toResponseForViewInMain(Product entity, LanguageCode languageCode) {
        ProductTranslation translation = entity.getProductTranslations().stream()
                .filter(l -> l.getLanguageCode() == languageCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + languageCode));
        Taste taste = entity.getTastes().stream().filter(t -> t.getLanguageCode().equals(languageCode)).findFirst().orElse(null);
        Discount discount = entity.getDiscounts().stream().filter(d -> d.getLanguageCode().equals(languageCode)).findFirst().orElse(null);

        List<HistoryPrices> prices = entity.getHistoryPrices();
        int priceNew = prices.size() > 0 ? prices.get(0).getCurrentPrice() : 0;
        int priceOld = prices.size() > 1 ? prices.get(1).getCurrentPrice() : 0;
        return ProductResponseForViewInTable
                .builder()
                .id(entity.getId())
                .articleId(entity.getArticleId())
                .name(translation.getName())
                .taste(taste != null ? taste.getName() : "")
                .discount(discount != null ? discount.getName() : "")
                .mass(entity.getMass())
                .pathToImage1(entity.getPathToImage1())
                .pathToImage2(entity.getPathToImage2())
                .pathToImage3(entity.getPathToImage3())
                .pathToImage4(entity.getPathToImage4())
                .priceNew(priceNew)
                .priceOld(priceOld)
                .build();
    }

    public ProductResponseForView toResponseForView(Product entity, LanguageCode languageCode) {
        ProductTranslation translation = entity.getProductTranslations().stream()
                .filter(l -> l.getLanguageCode() == languageCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + languageCode));
        Taste taste = entity.getTastes().stream().filter(t -> t.getLanguageCode().equals(languageCode)).findFirst().orElse(null);
        Discount discount = entity.getDiscounts().stream().filter(t -> t.getLanguageCode().equals(languageCode)).findFirst().orElse(null);

        List<HistoryPrices> prices = entity.getHistoryPrices();
        int priceOld = prices.size() > 0 ? prices.get(0).getCurrentPrice() : 0;
        int priceNew = prices.size() > 1 ? prices.get(1).getCurrentPrice() : 0;
        return ProductResponseForView
                .builder()
                .id(entity.getId())
                .articleId(entity.getArticleId())
                .amount(entity.getTotalCount())
                .name(translation.getName())
                .taste(taste != null ? taste.getName() : "")
                .discount(discount != null ? discount.getName() : "")
                .mass(String.valueOf(entity.getMass()))
                .energyMass(String.valueOf(entity.getMassEnergy()))
                .pathToImage1(entity.getPathToImage1())
                .pathToImage2(entity.getPathToImage2())
                .pathToImage3(entity.getPathToImage3())
                .pathToImage4(entity.getPathToImage4())
                .pathToImageDelivery(entity.getPathToImageDelivery())
                .pathToImageDescription(entity.getPathToImageDescription())
                .pathToImagePayment(entity.getPathToImagePayment())
                .pathToImagePacking(entity.getPathToImagePacking())
                .newPrice(String.valueOf(priceNew))
                .oldPrice(String.valueOf(priceOld))
                .descriptionProduct(translation.getDescriptionProduct())
                .descriptionDelivery(translation.getDescriptionDelivery())
                .descriptionPacking(translation.getDescriptionPacking())
                .descriptionPayment(translation.getDescriptionPayment())
                .recipe(translation.getRecipe())
                .conditionExploitation(translation.getConditionExploitation())
                .build();
    }

    public ProductResponseForViewInTable toResponseForViewInProduction(Product entity, LanguageCode languageCode) {
        ProductTranslation translation = entity.getProductTranslations().stream()
                .filter(l -> l.getLanguageCode() == languageCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + languageCode));
        Taste taste = entity.getTastes().stream().filter(t -> t.getLanguageCode().equals(languageCode)).findFirst().orElse(null);
        Discount discount = entity.getDiscounts().stream().filter(d -> d.getLanguageCode().equals(languageCode)).findFirst().orElse(null);

        List<HistoryPrices> prices = entity.getHistoryPrices().stream()
                .sorted(Comparator.comparing(HistoryPrices::getId).reversed()).toList();
        int priceNew = prices.size() > 0 ? prices.get(0).getCurrentPrice() : 0;
        int priceOld = prices.size() > 1 ? prices.get(1).getCurrentPrice() : 0;
        return ProductResponseForViewInTable
                .builder()
                .id(entity.getId())
                .articleId(entity.getArticleId())
                .name(translation.getName())
                .taste(taste != null ? taste.getName() : "")
                .discount(discount != null ? discount.getName() : "")
                .mass(entity.getMass())
                .pathToImage1(entity.getPathToImage1())
                .pathToImage2(entity.getPathToImage2())
                .pathToImage3(entity.getPathToImage3())
                .pathToImage4(entity.getPathToImage4())
                .priceNew(priceNew)
                .priceOld(priceOld)
                .build();
    }

    private void updateBasicFields(Product entity, ProductRequestForAdd dto) {
        entity.getProductTranslations().clear();
        entity.getProductTranslations().add(new ProductTranslation(
                null, LanguageCode.uk, dto.getNameUk(), dto.getRecipeUk(), dto.getConditionExploitationUk(), dto.getDescriptionProductUk(),
                dto.getDescriptionPackingUk(), dto.getDescriptionPaymentUk(), dto.getDescriptionDeliveryUk(), entity
        ));
        entity.getProductTranslations().add(new ProductTranslation(
                null, LanguageCode.en, dto.getNameEn(), dto.getRecipeEn(), dto.getConditionExploitationEn(), dto.getDescriptionProductEn(),
                dto.getDescriptionPackingEn(), dto.getDescriptionPaymentEn(), dto.getDescriptionDeliveryEn(), entity
        ));
    }

    private ProductTranslation getTranslation(Product entity, LanguageCode code) {
        return entity.getProductTranslations().stream()
                .filter(t -> t.getLanguageCode().equals(code))
                .findFirst()
                .orElseGet(() -> {
                    LogUtil.logError("Missing translation for language: " + code + " in Product ID: " + entity.getId(), null);
                    return new ProductTranslation();
                });
    }
}
