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
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ProductMapper {
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
        ProductTranslation productInUk = entity.getProductTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().get();
        ProductTranslation productInEn = entity.getProductTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().get();
        ProductResponseForAdd dto = ProductResponseForAdd
                .builder()
                .id(entity.getId())
                .articleId(entity.getArticleId())
                .isActive(entity.isActive())
                .amount(String.valueOf(entity.getTotalCount()))
                .mass(String.valueOf(entity.getMass()))
                .energyMass(String.valueOf(entity.getMassEnergy()))
                .nameUk(productInUk.getName())
                .recipeUk(productInUk.getRecipe())
                .conditionExploitationUk(productInUk.getConditionExploitation())
                .descriptionProductUk(productInUk.getDescriptionProduct())
                .descriptionPackingUk(productInUk.getDescriptionPacking())
                .descriptionPaymentUk(productInUk.getDescriptionPayment())
                .descriptionDeliveryUk(productInUk.getDescriptionDelivery())
                .nameEn(productInEn.getName())
                .recipeEn(productInEn.getRecipe())
                .descriptionProductEn(productInEn.getDescriptionProduct())
                .conditionExploitationEn(productInEn.getConditionExploitation())
                .descriptionPackingEn(productInEn.getDescriptionPacking())
                .descriptionPaymentEn(productInEn.getDescriptionPayment())
                .descriptionDeliveryEn(productInEn.getDescriptionDelivery())
                .pathToImage1(entity.getPathToImage1())
                .pathToImage2(entity.getPathToImage2())
                .pathToImage3(entity.getPathToImage3())
                .pathToImage4(entity.getPathToImage4())
                .pathToImageDescription(entity.getPathToImageDescription())
                .pathToImagePacking(entity.getPathToImagePacking())
                .pathToImagePayment(entity.getPathToImagePayment())
                .pathToImageDelivery(entity.getPathToImageDelivery())
                .build();
        entity.getTastes().stream().findFirst().ifPresent(t -> dto.setTasteId(t.getCommonId()));
        entity.getDiscounts().stream().findFirst().ifPresent(d -> dto.setDiscountId(d.getDiscountCommonId()));
        return dto;
    }

    public Product toEntityForRequestAdd(ProductRequestForAdd dto) {
        Product entity = new Product();
        entity.setId(dto.getId());
        entity.setArticleId(dto.getArticleId());
        entity.setActive(dto.getIsActive());
        entity.setTotalCount(dto.getAmount());
        ProductTranslation productUk = new ProductTranslation(LanguageCode.uk,
                dto.getNameUk(), dto.getRecipeUk(), dto.getConditionExploitationUk(), dto.getDescriptionProductUk(),
                dto.getDescriptionPackingUk(), dto.getDescriptionPaymentUk(), dto.getDescriptionDeliveryUk(), entity);
        ProductTranslation productEn = new ProductTranslation(LanguageCode.en,
                dto.getNameEn(), dto.getRecipeEn(), dto.getConditionExploitationEn(), dto.getDescriptionProductEn(),
                dto.getDescriptionPackingEn(), dto.getDescriptionPaymentEn(), dto.getDescriptionDeliveryEn(), entity);

        entity.setProductTranslations(List.of(productUk, productEn));
        entity.setCreatedDate(LocalDateTime.now());

        entity.setMass(dto.getMass().intValue());
        entity.setMassEnergy(dto.getEnergyMass().intValue());

        entity.setPathToImage1(dto.getPathToImage1());
        entity.setPathToImage2(dto.getPathToImage2());
        entity.setPathToImage3(dto.getPathToImage3());
        entity.setPathToImage4(dto.getPathToImage4());
        entity.setPathToImageDescription(dto.getPathToImageDescription());
        entity.setPathToImagePacking(dto.getPathToImagePacking());
        entity.setPathToImagePayment(dto.getPathToImagePayment());
        entity.setPathToImageDelivery(dto.getPathToImageDelivery());
        if (dto.getId() == null) {
            entity.setHistoryPrices(List.of(new HistoryPrices(dto.getNewPrice(), LocalDateTime.now(), LocalDateTime.now().plusDays(30), entity)));
        }
        entity.setPriceByUnit(String.valueOf(dto.getNewPrice()));
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
}
