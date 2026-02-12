package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.web.NewResponseForView;
import org.example.black_sea_walnut.dto.web.NewResponseInWeb;
import org.example.black_sea_walnut.dto.web.ResponseNewForViewInWeb;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.ContactService;
import org.example.black_sea_walnut.service.NewService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @GetMapping("/new/{id}")
    public ModelAndView viewNewsPage(@PathVariable Long id) {
        return new ModelAndView("web/news/one-new");
    }

    @GetMapping("/news/table/load")
    public ResponseEntity<PageResponse<ResponseNewForViewInWeb>> loadTable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(name = "lang") String languageCode) {
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseNewForViewInWeb> pageResponse = newService.getAllByActive(pageable, LanguageCode.fromString(languageCode),true);
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

    @GetMapping("/new/{id}/data")
    public ResponseEntity<?> getNewsPageDate(@RequestParam(name = "lang") String code,
                                             @PathVariable Long id) {
        ContactDtoForAdd contacts = contactService.getByIdInDto(1L);
        NewResponseInWeb new_ = newService.getByIdInResponseForWeb(id, LanguageCode.fromString(code));
        List<NewResponseInWeb> news = newService.getAllBySizeAmongLast(2, LanguageCode.fromString(code),id);
        return new ResponseEntity<>(NewResponseForView
                .builder()
                .contacts(contacts)
                .news(news)
                .new_(new_)
                .build(), HttpStatus.OK);
    }
}
