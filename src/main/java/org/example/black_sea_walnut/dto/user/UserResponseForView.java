package org.example.black_sea_walnut.dto.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseForView {
    private Long id;
    private String fullName;
    private String email;
    private String dateOfRegistration;
    private String registrationType;
    private String amountOrders;
    private String userStatus;
    private String phone;
}
