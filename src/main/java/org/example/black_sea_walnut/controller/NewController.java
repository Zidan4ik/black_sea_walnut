package org.example.black_sea_walnut.controller;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.ResponseNewForAdd;
import org.example.black_sea_walnut.dto.ResponseNewForView;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.NewService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Objects;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class NewController {
    private final NewService newService;

    @GetMapping("news/data")
    public ResponseEntity<PageResponse<ResponseNewForView>> getAll(
            @ModelAttribute ResponseNewForView responseNewForView,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam String languageCode,
            Model model
//            @RequestParam(name = "id") String id,
//            @RequestParam(name = "title") String title,
//            @RequestParam(name = "dateOfPublication") String date
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseNewForView> pageResponse = newService.getAll(responseNewForView, pageable, LanguageCode.fromString(languageCode));
        model.addAttribute("pageData", pageResponse.getMetadata());
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/news")
    public ModelAndView viewPage() {
        return new ModelAndView("admin/news/news");
    }

    @GetMapping("/news/table")
    public ModelAndView updateTable(@ModelAttribute ResponseNewForView responseNewForView,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseNewForView> pageResponse = newService.getAll(responseNewForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("news", pageResponse.getContent());
        model.addObject("pageData",pageResponse.getMetadata());
        return model;
    }

    @GetMapping("/new/create")
    public ModelAndView createNew() {
        return new ModelAndView("admin/news/new");
    }

    @PostMapping(value = "/new/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseNewForAdd> saveNew(@ModelAttribute ResponseNewForAdd dto) throws IOException {
        if (dto.getFile() != null) {
            if (Objects.requireNonNull(dto.getFile().getContentType()).contains("image")) {
                dto.setMediaType(org.example.black_sea_walnut.enums.MediaType.image);
            } else if (Objects.requireNonNull(dto.getFile().getContentType()).contains("video")) {
                dto.setMediaType(org.example.black_sea_walnut.enums.MediaType.video);
            }
        }
        newService.saveImage(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
