package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.ResponseNewForAdd;
import org.example.black_sea_walnut.dto.ResponseNewForView;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.entity.translation.NewTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;

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

        DateTimeFormatter inputDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        LocalDate date = entity.getDateOfPublication();

        LocalDate dateOutput = LocalDate.parse(date.toString(), inputDate);

        return ResponseNewForView.builder().id(entity.getId())
                .title(translation.getTitle())
                .description(translation.getDescription())
                .code(code)
                .isActive(entity.isActive())
                .date(dateOutput.format(outputDate))
                .id(entity.getId())
                .build();
    }

    public ResponseNewForAdd toDtoAdd(New entity, LanguageCode code) {
        NewTranslation translation = entity.getTranslations()
                .stream()
                .filter(c -> c.getLanguageCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + code));

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate parsedDate = LocalDate.parse(entity.getDateOfPublication().toString(), inputFormat);

        return ResponseNewForAdd.builder()
                .id(entity.getId())
                .idTranslation(translation.getId())
                .code(code)
                .isActive(entity.isActive())
                .title(translation.getTitle())
                .description(translation.getDescription())
                .mediaType(entity.getMediaType())
                .pathToImage(entity.getPathToMedia())
                .dateOfPublication(parsedDate.format(outputFormat))
                .build();
    }

    public New toEntity(ResponseNewForAdd dto) {
        New new_ = new New();
        new_.setId(dto.getId());
        new_.setActive(dto.getIsActive());
        new_.setMediaType(dto.getMediaType());
        new_.setPathToMedia(dto.getPathToImage());

        NewTranslation translation = new NewTranslation(
                dto.getId(), dto.getCode(), dto.getTitle(), dto.getDescription(), new_
        );
        new_.setTranslations(List.of(translation));

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(dto.getDateOfPublication(), inputFormat);

        dto.setDateOfPublication(parsedDate.format(outputFormat));
        return new_;
    }
}
