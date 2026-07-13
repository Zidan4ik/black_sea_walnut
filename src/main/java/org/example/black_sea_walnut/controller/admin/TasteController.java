package org.example.black_sea_walnut.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.taste.TasteRequestForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForView;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.TasteMapper;
import org.example.black_sea_walnut.service.document.excel.TasteExcelExporter;
import org.example.black_sea_walnut.service.product.taste.TasteService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class TasteController {
    private final TasteService tasteService;
    private final TasteMapper tasteMapper;
    private final TasteExcelExporter tasteExcelExporter;

    @GetMapping("/taste/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcelTaste(){
        List<Taste> tastes = tasteService.getAll();
        ByteArrayInputStream in = tasteExcelExporter.exportTastes(tastes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tastes_report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @PostMapping("/taste/save")
    public ResponseEntity<?> saveTaste(@Valid TasteRequestForAdd dto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity
                    .status(HttpStatus.valueOf(400))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errors);
        }
        tasteService.save(dto,tasteMapper);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/taste/{id}")
    public ResponseEntity<TasteResponseForAdd> getTaste(@PathVariable Long id) {
        TasteResponseForAdd dto = tasteService.getByIdInResponseForAdd(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/tastes")
    public ResponseEntity<Set<TasteResponseForView>> getTastes(@RequestParam String languageCode) {
        Set<TasteResponseForView> tastes = tasteService.getAllByLanguageCodeInDTO(LanguageCode.fromString(languageCode),tasteMapper::toDTOForView);
        return new ResponseEntity<>(tastes, HttpStatus.OK);
    }

    @DeleteMapping("/taste/{id}/delete")
    public ResponseEntity<String> deleteTasteById(@PathVariable Long id) {
        tasteService.deleteByCommonId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/taste/delete")
    public ResponseEntity<?> deleteTasteById2(@RequestBody Long id){
        tasteService.deleteByCommonId(id);
        return new ResponseEntity<>("Taste was successful deleted", HttpStatus.OK);
    }
}
