package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.admin.order.ResponseOrderDetailForView;
import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.entity.OrderDetail;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper {
    public static ResponseOrderDetailForView toDTOView(OrderDetail entity){
        return ResponseOrderDetailForView
                .builder()
                .id(entity.getId())
                .productName(entity.getProductName())
                .count(entity.getCount())
                .price(entity.getUnitPrice())
                .totalSumma(entity.getSummaWithDiscount())
                .mass(entity.getMass())
                .build();
    }

    public static OrderDetail toEntityForBasket(BasketResponseForCart dto){
        OrderDetail entity = new OrderDetail();
        entity.setProductName(entity.getProductName());
        entity.setCount(dto.getAmount());
        entity.setUnitPrice(dto.getPrice());
        entity.setMass(dto.getMass());

//        entity.set
        return entity;
    }
}