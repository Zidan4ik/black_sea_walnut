package org.example.black_sea_walnut.dto.web.checkout;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.validator.annotation.EmailValidation;
import org.example.black_sea_walnut.validator.annotation.IsNoExistEmail;
import org.example.black_sea_walnut.validator.groupValidation.EmailValidGroups;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutUser {
    private List<BasketResponseForCart> products;
    private Long totalAmount;
    private Long totalCount;
    @NotBlank(message = "{error.field.empty}")
    private String fio;
    @NotBlank(message = "{error.field.empty}", groups = EmailValidGroups.NotBlankCheck.class)
    @EmailValidation(groups = EmailValidGroups.EmailCheck.class)
    @IsNoExistEmail(groups = EmailValidGroups.EmailExistenceCheck.class)
    private String email;
    private String phone;
    private String typeOfDelivery;
    private String countryId;
    private String regionId;
    private String cityId;
    private String typeOfPayment;
    private Boolean isPayed;
    private String companyDelivery;
    private String personNameDelivery;
    private User user;
}
