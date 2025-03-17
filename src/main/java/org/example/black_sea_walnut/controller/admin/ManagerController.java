package org.example.black_sea_walnut.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.manager.ManagerDTO;
import org.example.black_sea_walnut.dto.admin.manager.ManagerResponseForView;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.ManagerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/managers")
    public ModelAndView showManagers() {
        return new ModelAndView("admin/managers/managers");
    }

    @GetMapping("/managers/table/load")
    public ModelAndView loadTable(@ModelAttribute ManagerResponseForView managerResponseForView,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-managers");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ManagerResponseForView> pageResponse =
                managerService.getAll(managerResponseForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("data", pageResponse.getContent());
        return model;
    }

    @GetMapping("/managers/pagination/load")
    public ModelAndView loadPagination(@ModelAttribute ManagerResponseForView managerResponseForView,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<ManagerResponseForView> pageResponse =
                managerService.getAll(managerResponseForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }

    @DeleteMapping("/manager/{id}/delete")
    public ResponseEntity<?> deleteManager(@PathVariable Long id) {
        managerService.deleteById(id);
        return new ResponseEntity<>("Manager was successful deleted!", HttpStatus.OK);
    }

    @PostMapping("/manager/save")
    public ResponseEntity<?> saveManager(@Valid @ModelAttribute ManagerDTO dto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        managerService.save(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/manager/{id}")
    public ResponseEntity<ManagerDTO> getManager(@PathVariable Long id) {
        return new ResponseEntity<>(managerService.getByIdInResponseForAdd(id), HttpStatus.OK);
    }

    @ModelAttribute("isActiveManagers")
    public boolean toActiveApplications() {
        return true;
    }
}
