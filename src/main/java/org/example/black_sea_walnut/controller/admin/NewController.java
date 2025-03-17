package org.example.black_sea_walnut.controller.admin;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.admin.new_.ResponseNewForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.NewService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseNewForView> pageResponse = newService.getAll(responseNewForView, pageable, LanguageCode.fromString(languageCode));
        model.addAttribute("pageData", pageResponse.getMetadata());
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/news")
    public ModelAndView viewPage() {
        return new ModelAndView("admin/news/table");
    }

    @GetMapping("/news/table/load")
    public ModelAndView loadTable(@ModelAttribute ResponseNewForView responseNewForView,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-news");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseNewForView> pageResponse = newService.getAll(responseNewForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("data", pageResponse.getContent());
        return model;
    }

    @GetMapping("/news/pagination/load")
    public ModelAndView loadPagination(@ModelAttribute ResponseNewForView responseNewForView,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseNewForView> pageResponse = newService.getAll(responseNewForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }

    @GetMapping("/new/create")
    public ModelAndView createNew() {
        return new ModelAndView("admin/news/new-add");
    }

    @GetMapping("/new/edit/{id}")
    public ModelAndView editNew(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("admin/news/new-edit");
        NewRequestForAdd byIdLikeDTO = newService.getByIdLikeDTO(id);
        model.addObject("news", byIdLikeDTO);
        return model;
    }

    @GetMapping("/new/delete/{id}")
    public ResponseEntity<?> deleteNew(@PathVariable Long id) {
        newService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/new/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveNew(@ModelAttribute @Valid NewRequestForAdd dto,
                                     BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }

        HttpHeaders headers = new HttpHeaders();
        if (dto.getFile() != null) {
                    if (Objects.requireNonNull(dto.getFile().getContentType()).contains("image")) {
                        dto.setMediaType(org.example.black_sea_walnut.enums.MediaType.image);
                    } else if (Objects.requireNonNull(dto.getFile().getContentType()).contains("video")) {
                        dto.setMediaType(org.example.black_sea_walnut.enums.MediaType.video);
                    }
        }
        newService.saveImage(dto);
        headers.add("HX-Redirect", "/admin/news");
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
    @ModelAttribute("isActiveNews")
    public boolean toActiveSidebarButton(){
        return true;
    }
}
