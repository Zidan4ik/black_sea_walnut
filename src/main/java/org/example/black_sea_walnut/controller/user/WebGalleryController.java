package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.GalleryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebGalleryController {
    private final GalleryService galleryService;

    @GetMapping("/gallery")
    public ModelAndView viewGallery() {
        return new ModelAndView("web/gallery/gallery");
    }

    @GetMapping("/gallery/load")
    public ModelAndView getDataForGalleryPage(Pageable pageable) {
        ModelAndView model = new ModelAndView("web/gallery/gallery-list");
        PageResponse<GalleryResponseForAdd> response = galleryService.getAllInResponseByLanguageCode(pageable, LanguageCode.uk);
        model.addObject("gallery",response.getContent());
        model.addObject("totalPages",response.getMetadata().getTotalPages());
        return model;
    }
}