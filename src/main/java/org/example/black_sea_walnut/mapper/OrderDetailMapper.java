package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.order.ResponseOrderDetailForView;
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
}