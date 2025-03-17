package org.example.black_sea_walnut.dto.admin.user.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.admin.order.OrderUserResponseForView;
import org.example.black_sea_walnut.enums.Role;

import java.util.List;

@Builder
@Getter
public class UserIndividualResponseForAdd {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private Long regionForDeliveryId;
    private Long cityForDeliveryId;
    private int departmentForDelivery;
    private String registrationType;
    private String status;
    private String pathToImage;
    private String password;
    private Role role;
    private List<OrderUserResponseForView> orders;
}
