package org.example.black_sea_walnut.dto.admin.taste;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.mapper.TasteMapper;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.validator.annotation.IsExistTasteValidation;
import org.example.black_sea_walnut.validator.annotation.NumberNullValidation;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Builder
@Getter
@IsExistTasteValidation
public class TasteRequestForAdd implements Saveable<Taste, TasteMapper> {
    private Long tasteIdUk;
    private Long tasteIdEn;
    @NumberNullValidation(message ="{error.field.empty.number}")
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long commonId;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String tasteNameUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String tasteNameEn;

    @Override
    public Long getId() {
        return this.getTasteIdUk();
    }

    @Override
    public List<Taste> updateAndGetList(TasteMapper mapper) {
       return mapper.toEntityFromRequest(this);
    }
}
