package org.example.black_sea_walnut.dto.admin.stripe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseForStripe {
    private String status;
    private String message;
    private String sessionId;
    private String sessionUrl;
}
