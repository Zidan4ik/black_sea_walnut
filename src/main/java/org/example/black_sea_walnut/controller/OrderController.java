package org.example.black_sea_walnut.controller;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.order.ResponseOrderForAdd;
import org.example.black_sea_walnut.dto.order.ResponseOrderForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/orders")
    public ModelAndView viewOrders() {
        return new ModelAndView("/admin/orders/table");
    }

    @GetMapping("/orders/table/load")
    public ModelAndView loadTable(@ModelAttribute ResponseOrderForView responseOrderForView,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-orders");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseOrderForView> pageResponse = orderService.getAll(responseOrderForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("data", pageResponse.getContent());
        return model;
    }

    @GetMapping("/orders/pagination/load")
    public ModelAndView loadPagination(@ModelAttribute ResponseOrderForView responseOrderForView,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ResponseOrderForView> pageResponse = orderService.getAll(responseOrderForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }


    @GetMapping("/order/{id}")
    public ModelAndView viewOrder(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("admin/orders/view");
        model.addObject("id", id);
        return model;
    }

    @GetMapping("/order/{id}/data")
    public ResponseEntity<ResponseOrderForAdd> getOrder(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getByIdInDTOAdd(id), HttpStatus.OK);
    }

    @GetMapping("/order/delete/{id_}")
    public ModelAndView deleteOrder(@PathVariable(name = "id_") Long id) {
        orderService.deleteById(id);
        return new ModelAndView("redirect:/admin/orders");
    }
    @ModelAttribute("isActiveOrders")
    public boolean toActiveApplications(){
        return true;
    }
}
