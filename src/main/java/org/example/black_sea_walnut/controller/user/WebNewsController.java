package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.web.NewResponseForView;
import org.example.black_sea_walnut.dto.web.ResponseNewForViewInWeb;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.ContactService;
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
@RequestMapping("/web")
public class WebNewsController {
    private final NewService newService;
    private final ContactService contactService;

    @GetMapping("/news")
    public ModelAndView viewNewsPage() {
        return new ModelAndView("web/news/news");
    }

    @GetMapping("/news/table/load")
    public ResponseEntity<PageResponse<ResponseNewForViewInWeb>> loadTable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(name = "lang") String languageCode) {
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseNewForViewInWeb> pageResponse = newService.getAll(pageable, LanguageCode.fromString(languageCode));
//        model.addObject("data", pageResponse.getContent());
//        model.addObject("totalPages", pageResponse.getMetadata().getTotalPages());
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/news/data")
    public ResponseEntity<?> getNewsPageDate(@RequestParam(name = "lang") String code) {
        ContactDtoForAdd contacts = contactService.getByIdInDto(1l);
        return new ResponseEntity<>(NewResponseForView
                .builder()
                .contacts(contacts)
                .build(), HttpStatus.OK);
    }
}
