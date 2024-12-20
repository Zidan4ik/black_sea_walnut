package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.ResponseNewForAdd;
import org.example.black_sea_walnut.dto.ResponseNewForView;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.entity.translation.NewTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NewMapper {
    public ResponseNewForView toDtoView(New entity, LanguageCode code) {
        NewTranslation translation = entity.getTranslations()
                .stream()
                .filter(c -> c.getLanguageCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + code));
        return ResponseNewForView.builder().id(entity.getId())
                .title(translation.getTitle())
                .description(translation.getDescription())
                .code(code)
                .isActive(entity.isActive())
                .date(DateUtil.toFormatDateFromDB(entity.getDateOfPublication(),"dd.MM.yyyy"))
                .id(entity.getId())
                .build();
    }

    public ResponseNewForAdd toDtoAdd(New entity) {
        ResponseNewForAdd builder = ResponseNewForAdd.builder()
                .id(entity.getId())
                .isActive(entity.isActive())
                .mediaType(entity.getMediaType())
                .pathToImage(entity.getPathToMedia())
                .dateOfPublication(DateUtil.toFormatDateFromDB(entity.getDateOfPublication(),"dd.MM.yyyy"))
                .build();

        for (NewTranslation t: entity.getTranslations()){
            switch (t.getLanguageCode()){
                case uk -> {
                    builder.setTitleUA(t.getTitle());
                    builder.setDescriptionUA(t.getDescription());
                }
                case en -> {
                    builder.setTitleENG(t.getTitle());
                    builder.setDescriptionENG(t.getDescription());
                }
            }
        }

        return builder;
    }

    public New toEntity(ResponseNewForAdd dto) {
        New new_ = new New();
        new_.setId(dto.getId());
        new_.setActive(dto.isActive());
        new_.setMediaType(dto.getMediaType());
        new_.setPathToMedia(dto.getPathToImage());

        NewTranslation translationUA = new NewTranslation(
                LanguageCode.uk, dto.getTitleUA(), dto.getDescriptionUA(), new_
        );
        NewTranslation translationEN = new NewTranslation(
                LanguageCode.en, dto.getTitleENG(), dto.getDescriptionENG(), new_
        );
        new_.setTranslations(List.of(translationUA,translationEN));
        new_.setDateOfPublication(DateUtil.toFormatDateToDB(dto.getDateOfPublication(),"dd.MM.yyyy"));
        return new_;
    }
}
