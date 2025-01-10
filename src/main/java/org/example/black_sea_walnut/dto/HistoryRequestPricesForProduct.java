package org.example.black_sea_walnut.dto;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.example.black_sea_walnut.validator.NumberNullValidation;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRequestPricesForProduct {
    private Long id;
    @Setter
    private Long productId;
    @NumberNullValidation
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long newPrice;
    @NumberNullValidation
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long oldPrice;
}