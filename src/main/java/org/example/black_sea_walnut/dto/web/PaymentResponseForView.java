package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.response.AimBlockResponseForAddInMain;
import org.example.black_sea_walnut.dto.admin.pages.main.response.NumberBlockResponseForAddInMain;

@Builder
@Data
public class PaymentResponseForView {
    private AimBlockResponseForAddInMain aim;
    private NumberBlockResponseForAddInMain numbers;
    private ContactDtoForAdd contacts;
}
