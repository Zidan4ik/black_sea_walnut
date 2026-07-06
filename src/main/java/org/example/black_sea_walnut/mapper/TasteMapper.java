package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.admin.taste.TasteRequestForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForView;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class TasteMapper implements GenericsMapper {
    public TasteResponseForView toDTOForView(Taste entity) {
        return TasteResponseForView
                .builder()
                .id(entity.getCommonId())
                .name(entity.getName())
                .build();
    }

    public List<Taste> toEntityFromRequest(TasteRequestForAdd dto) {
        return List.of(new Taste(dto.getTasteIdUk(),dto.getCommonId(),LanguageCode.uk,dto.getTasteNameUk())
                , new Taste(dto.getTasteIdEn(),dto.getCommonId(),LanguageCode.en,dto.getTasteNameEn()));
    }

    public TasteResponseForAdd toResponseForAdd(Set<Taste> entities) {
        Taste tasteUk = entities.stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        Taste tasteEn = entities.stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        if (tasteUk != null && tasteEn != null) {
            return TasteResponseForAdd
                    .builder()
                    .tasteIdUk(tasteUk.getId())
                    .tasteIdEn(tasteEn.getId())
                    .commonId(tasteUk.getCommonId())
                    .tasteNameUk(tasteUk.getName())
                    .tasteNameEn(tasteEn.getName())
                    .build();
        }
        return null;
    }
}
