package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.admin.manager.ManagerDTO;
import org.example.black_sea_walnut.dto.admin.manager.ManagerResponseForView;
import org.example.black_sea_walnut.entity.Manager;
import org.example.black_sea_walnut.entity.translation.ManagerTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManagerMapper {
    public ManagerDTO toResponseForDTO(Manager entity) {
        ManagerTranslation translationUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        ManagerTranslation translationEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        return ManagerDTO
                .builder()
                .id(entity.getId())
                .phone(entity.getPhone())
                .nameUk(translationUk != null ? translationUk.getName() : null)
                .nameEn(translationEn != null ? translationEn.getName() : null)
                .surnameUk(translationUk != null ? translationUk.getSurname() : null)
                .surnameEn(translationEn != null ? translationEn.getSurname() : null)
                .build();
    }

    public ManagerResponseForView toResponseForView(Manager entity, LanguageCode code) {
        ManagerTranslation manager = entity.getTranslations().stream().filter(m -> m.getLanguageCode().equals(code)).findFirst().orElse(null);
        return ManagerResponseForView
                .builder()
                .id(entity.getId())
                .name(manager != null ? manager.getName() : null)
                .surname(manager != null ? manager.getSurname() : null)
                .phone(entity.getPhone())
                .build();
    }

    public Manager toEntityFromRequest(ManagerDTO dto) {
        Manager entity = new Manager();
        entity.setId(dto.getId());
        entity.setPhone(dto.getPhone());
        ManagerTranslation translationUk = new ManagerTranslation(LanguageCode.uk, dto.getNameUk(), dto.getSurnameUk(), entity);
        ManagerTranslation translationEn = new ManagerTranslation(LanguageCode.en, dto.getNameEn(), dto.getSurnameEn(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        return entity;
    }
}