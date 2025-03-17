package org.example.black_sea_walnut.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class GalleryController {
    private final GalleryService galleryService;

    @GetMapping("/galleries")
    public ModelAndView viewGallery() {
        return new ModelAndView("admin/galleries/galleries");
    }

    @GetMapping("/galleries/load")
    public ModelAndView loadGalleries(@RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/galleries/fragment-galleries");
        List<GalleryResponseForAdd> galleries = galleryService.getAllInResponseByLanguageCode(LanguageCode.fromString(languageCode));
        model.addObject("list", galleries);
        return model;
    }

    @PostMapping("/gallery/save")
    public ResponseEntity<?> saveGallery(@Valid GalleryRequestForAdd dto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        if (dto.getFile() != null) {
            if (Objects.requireNonNull(dto.getFile().getContentType()).contains("image")) {
                dto.setMediaType(org.example.black_sea_walnut.enums.MediaType.image);
            } else if (Objects.requireNonNull(dto.getFile().getContentType()).contains("video")) {
                dto.setMediaType(org.example.black_sea_walnut.enums.MediaType.video);
            }
        }
        galleryService.save(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/gallery/{id}/delete")
    public ResponseEntity<String> deleteGalleryById(@PathVariable Long id) {
        galleryService.deleteById(id);
        return new ResponseEntity<>("A gallery with id:" + id + " was successful deleted!", HttpStatus.OK);
    }
    @ModelAttribute("isActiveGallery")
    public boolean toActiveSidebarButton(){
        return true;
    }
}
