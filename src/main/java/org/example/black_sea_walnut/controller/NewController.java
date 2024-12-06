package org.example.black_sea_walnut.controller;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.ResponseNewForView;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.NewService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class NewController {
    private final NewService newService;

    @GetMapping("get/news")
    public ResponseEntity<PageResponse<ResponseNewForView>> getAll(
            @ModelAttribute ResponseNewForView responseNewForView,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam String languageCode) {
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseNewForView> pageResponse = newService.getAll(responseNewForView, pageable, LanguageCode.fromString(languageCode));
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/news")
    public ModelAndView viewPage(){
        return new ModelAndView("admin/news/news");
    }

    @GetMapping("/new/create")
    public ModelAndView createNew(){
        return new ModelAndView("admin/news/new");
    }
}
