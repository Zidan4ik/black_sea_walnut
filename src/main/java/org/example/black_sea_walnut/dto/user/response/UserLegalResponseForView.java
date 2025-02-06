package org.example.black_sea_walnut.dto.user.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.order.OrderUserResponseForView;
import org.example.black_sea_walnut.enums.Role;

import java.util.List;

@Builder
@Getter
public class UserLegalResponseForView {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private Long regionForDeliveryId;
    private Long cityForDeliveryId;
    private String departmentForDelivery;
    private String registrationType;
    private String status;
    private String pathToImage;
    private String okpo;
    private Long regionAdditionallyId;
    private Long cityAdditionallyId;
    private String addressAdditionally;
    private String indexLawful;
    private String password;
    private Role role;
    private List<OrderUserResponseForView> orders;
}
