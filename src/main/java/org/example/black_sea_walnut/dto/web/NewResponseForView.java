package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;

@Builder
@Data
public class NewResponseForView {
    private ContactDtoForAdd contacts;
}
