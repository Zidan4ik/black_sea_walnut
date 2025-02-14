package org.example.black_sea_walnut.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.calls.CallResponseForView;
import org.example.black_sea_walnut.service.CallService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class CallController {
    private final CallService callService;

    @GetMapping("/calls")
    public ModelAndView showCalls() {
        return new ModelAndView("admin/calls/calls");
    }

    @GetMapping("/calls/table/load")
    public ModelAndView loadTable(@ModelAttribute CallResponseForView callResponseForView,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-calls");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<CallResponseForView> pageResponse = callService.getAll(callResponseForView, pageable);
        model.addObject("data", pageResponse.getContent());
        return model;
    }

    @GetMapping("/calls/pagination/load")
    public ModelAndView loadPagination(@ModelAttribute CallResponseForView callResponseForView,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<CallResponseForView> pageResponse = callService.getAll(callResponseForView, pageable);
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }

    @DeleteMapping("/call/{id}/delete")
    public ResponseEntity<?> deleteCall(@PathVariable Long id) {
        callService.deleteById(id);
        return new ResponseEntity<>("Call was successful deleted!", HttpStatus.OK);
    }
    @ModelAttribute("isActiveCalls")
    public boolean toActiveSidebarButton(){
        return true;
    }
}
