package org.example.black_sea_walnut.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.nut.NutRequestForAdd;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForAdd;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForView;
import org.example.black_sea_walnut.entity.Nut;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.NutService;
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
public class NutController {
    private final NutService nutService;

    @GetMapping("/nuts")
    public ModelAndView viewNuts() {
        return new ModelAndView("admin/nuts/nuts");
    }

    @GetMapping("/nuts/table/load")
    public ModelAndView loadTable(@ModelAttribute NutResponseForView nutResponseForView,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/table-nuts");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<NutResponseForView> pageResponse = nutService.getAll(nutResponseForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("data", pageResponse.getContent());
        return model;
    }

    @GetMapping("/nuts/pagination/load")
    public ModelAndView loadPagination(@ModelAttribute NutResponseForView nutResponseForView,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam String languageCode) {
        ModelAndView model = new ModelAndView("admin/fragments/pagination");
        PageRequest pageable = PageRequest.of(page, size);
        PageResponse<NutResponseForView> pageResponse = nutService.getAll(nutResponseForView, pageable, LanguageCode.fromString(languageCode));
        model.addObject("pageData", pageResponse.getMetadata());
        return model;
    }

    @GetMapping("/nut/create")
    public ModelAndView viewNut() {
        return new ModelAndView("admin/nuts/nut-add");
    }

    @GetMapping("/nut/{id}/edit")
    public ModelAndView viewNut(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("admin/nuts/nut-edit");
        model.addObject("id", id);
        return model;
    }

    @GetMapping("/nut/{id}")
    @ResponseBody
    public ResponseEntity<NutResponseForAdd> getNut(@PathVariable Long id) {
        return new ResponseEntity<>(nutService.getByIdInResponseDtoAdd(id), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("/nut/save")
    public ResponseEntity<?> saveProduct(@Valid NutRequestForAdd dto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        nutService.save(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("/nut/save/switch")
    public ResponseEntity<?> saveAfterSwitch(@RequestParam(name = "id") Long id,
                                             @RequestParam(name = "isActive") boolean isActive) {
        Nut nut = nutService.getById(id);
        nut.setActive(Boolean.valueOf(isActive));
        nutService.save(nut);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/nut/{id}/delete")
    public ResponseEntity<?> deleteNut(@PathVariable Long id) {
        nutService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/nut/delete")
    public ResponseEntity<?> deleteNut2(@RequestBody Long id) {
        nutService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ModelAttribute("isActiveNuts")
    public boolean toActiveSidebarButton() {
        return true;
    }
}
