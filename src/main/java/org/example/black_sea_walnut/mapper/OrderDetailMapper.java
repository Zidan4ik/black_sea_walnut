package org.example.black_sea_walnut.mapper;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.order.ResponseOrderDetailForView;
import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.entity.*;
import org.example.black_sea_walnut.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderDetailMapper {
    private final ProductService productService;

    public static ResponseOrderDetailForView toDTOView(OrderDetail entity) {
        return ResponseOrderDetailForView
                .builder()
                .id(entity.getId())
                .productName(entity.getProductNameUk())
                .count(entity.getCount())
                .price(entity.getUnitPrice())
                .totalSumma(entity.getSummaWithDiscount())
                .build();
    }

    public OrderDetail toEntityForBasket(BasketResponseForCart dto, Order order) {
        Product existingProduct = productService.getByArticleId(dto.getArticleProduct());

        OrderDetail entity = new OrderDetail();
        entity.setProductNameUk(entity.getProductNameUk());
        entity.setCount(dto.getAmount());
        entity.setUnitPrice(dto.getPrice());
        entity.setMass(dto.getMass());
        entity.setOrder(order);
        entity.setProducts(List.of(existingProduct));

        applyDiscounts(entity, existingProduct);
        return entity;
    }

    private void applyDiscounts(OrderDetail orderDetail, Product product) {
        int unitPrice = Integer.parseInt(product.getPriceByUnit());
        int discountPercent = product.getDiscounts().isEmpty() ? 0 : product.getDiscounts().iterator().next().getValue();
        int discountSumForUnit = (unitPrice * discountPercent) / 100;
        int discountUnitPrice = unitPrice - discountSumForUnit;
        int summaWithoutDiscount = unitPrice * orderDetail.getCount();
        int summaDiscount = discountSumForUnit * orderDetail.getCount();
        int summaWithDiscount = summaWithoutDiscount - summaDiscount;

        orderDetail.setUnitPrice(unitPrice);
        orderDetail.setDiscountPercent(discountPercent);
        orderDetail.setDiscountSumForUnit(discountSumForUnit);
        orderDetail.setDiscountUnitPrice(discountUnitPrice);
        orderDetail.setSummaWithoutDiscount(summaWithoutDiscount);
        orderDetail.setSummaDiscount(summaDiscount);
        orderDetail.setSummaWithDiscount(summaWithDiscount);
    }
}