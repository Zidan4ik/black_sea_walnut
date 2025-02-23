package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;

@Data
@Builder
public class ProductResponseInWeb {
    private ContactDtoForAdd contacts;
    private ProductResponseForView product;
}
