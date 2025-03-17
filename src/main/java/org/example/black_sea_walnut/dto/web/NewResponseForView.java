package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;

import java.util.List;

@Builder
@Data
public class NewResponseForView {
    private ContactDtoForAdd contacts;
    private NewResponseInWeb new_;
    private List<NewResponseInWeb> news;
}
