package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.entity.Basket;
import org.example.black_sea_walnut.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class BasketMapper {
    public BasketResponseForCart toResponseForCart(Basket entity){
        Product product = entity.getProducts().get(0);
        return BasketResponseForCart
                .builder()
                .id(entity.getId())
                .name(entity.getProductName())
                .amount(entity.getCount())
                .price(entity.getUnitPrice())
                .mass(entity.getMass())
                .articleProduct(product!=null?product.getArticleId():null)
                .build();
    }
}
