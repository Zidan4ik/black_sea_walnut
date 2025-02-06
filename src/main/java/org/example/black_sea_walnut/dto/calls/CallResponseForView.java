package org.example.black_sea_walnut.dto.calls;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CallResponseForView {
    private Long id;
    private String date;
    private String time;
    private String phone;
    private String status;
}
