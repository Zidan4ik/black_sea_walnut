package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.entity.Country;

import java.util.List;

@Builder
@Data
public class RegistrationResponseForView {
    private ContactDtoForAdd contacts;
    private List<Country> countries;
}

