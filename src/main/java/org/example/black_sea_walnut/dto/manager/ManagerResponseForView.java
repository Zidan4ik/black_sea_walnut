package org.example.black_sea_walnut.dto.manager;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManagerResponseForView {
    private Long id;
    private String name;
    private String surname;
    private String phone;
}
