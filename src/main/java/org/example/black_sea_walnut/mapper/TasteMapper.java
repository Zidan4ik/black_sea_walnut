package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.taste.ResponseTasteForView;
import org.example.black_sea_walnut.entity.Taste;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TasteMapper {
    public ResponseTasteForView toDTOForView(Taste entity){
        return ResponseTasteForView
                .builder()
                .id(entity.getTasteId())
                .name(entity.getName())
                .build();
    }
//    public ResponseTastesForProduct toDTOForProduct(List<Taste> tastes) {
//        Taste nameUk = tastes.stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("Taste with language code 'uk' not found"));
//        Taste nameEn = tastes.stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("Taste with language code 'en' not found"));
//        Long id = nameUk.getId().equals(nameUk.getProduct().getId()) ? nameUk.getProduct().getId() : null;
//        return ResponseTastesForProduct
//                .builder()
//                .commonId(id)
//                .nameUk(nameUk.getName())
//                .nameEn(nameEn.getName())
//                .build();
//    }

}
