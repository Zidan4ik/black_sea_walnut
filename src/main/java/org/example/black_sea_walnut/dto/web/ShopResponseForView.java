package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.dto.taste.TasteResponseForView;

import java.util.List;
import java.util.Set;

@Builder
@Data
public class ShopResponseForView {
    List<Integer> masses;
    Set<TasteResponseForView> tastes;
    BannerBlockResponseForAdd banner;
    EcologicallyBlockResponseForAdd ecologically;
    ContactDtoForAdd contacts;
}
