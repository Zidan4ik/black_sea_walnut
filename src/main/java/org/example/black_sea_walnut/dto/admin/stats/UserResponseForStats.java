package org.example.black_sea_walnut.dto.admin.stats;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class UserResponseForStats {
    private String status;
    private Long count;
}
