package org.example.black_sea_walnut.dto.admin.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserResponseForView {
    private Long id;
    private String fullName;
    private String email;
    private String date;
    private String registrationType;
    private String amountOrders;
    private String userStatus;
    private String phone;
}
