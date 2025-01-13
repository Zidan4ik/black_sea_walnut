package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.taste.TasteRequestForAdd;
import org.example.black_sea_walnut.dto.taste.TasteResponseForAdd;
import org.example.black_sea_walnut.dto.taste.TasteResponseForView;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class TasteMapper {
    public TasteResponseForView toDTOForView(Taste entity) {
        return TasteResponseForView
                .builder()
                .id(entity.getTasteId())
                .name(entity.getName())
                .build();
    }

    public List<Taste> toEntityFromRequest(TasteRequestForAdd dto) {
        Taste tasteUk = new Taste();
        tasteUk.setId(dto.getId());
        tasteUk.setTasteId(dto.getTasteId());
        tasteUk.setLanguageCode(LanguageCode.uk);
        tasteUk.setName(dto.getNameUk());

        Taste tasteEn = new Taste();
        tasteEn.setId(dto.getId());
        tasteEn.setTasteId(dto.getTasteId());
        tasteEn.setLanguageCode(LanguageCode.en);
        tasteEn.setName(dto.getNameEn());
        return List.of(tasteUk, tasteEn);
    }

    public TasteResponseForAdd toResponseForAdd(Set<Taste> entities) {
        Taste tasteUk = entities.stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        Taste tasteEn = entities.stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        if (tasteUk != null && tasteEn != null) {
            return TasteResponseForAdd
                    .builder()
                    .id(tasteUk.getId())
                    .tasteId(tasteUk.getTasteId())
                    .tasteNameUk(tasteUk.getName())
                    .tasteNameEn(tasteEn.getName())
                    .build();
        }
        return null;
    }
}
