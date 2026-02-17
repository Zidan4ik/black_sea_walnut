package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.entity.Basket;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.stereotype.Component;

@Component
public class BasketMapper {
    public BasketResponseForCart toResponseForCart(Basket entity, LanguageCode code){
        Product product = entity.getProducts().get(0);
        String productName = code.equals(LanguageCode.uk) ?
                entity.getProductNameUk() : entity.getProductNameEn();
        return BasketResponseForCart
                .builder()
                .id(entity.getId())
                .name(productName)
                .amount(entity.getCount())
                .price(entity.getUnitPrice())
                .mass(entity.getMass())
                .articleProduct(product!=null?product.getArticleId():null)
                .build();
    }
}
