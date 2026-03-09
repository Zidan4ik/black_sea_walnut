package org.example.black_sea_walnut.dto.admin.calls;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.mapper.CallMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.validator.annotation.PhoneFormatValidation;
import org.example.black_sea_walnut.validator.groupValidation.PhoneValidGroups;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
public class CallResponseForView implements Saveable<Call, CallMapper> {
    private Long id;
    private String date;
    private String time;
    @PhoneFormatValidation(groups = PhoneValidGroups.NotPhoneFormatValidation.class)
    @NotBlank(message = "{error.field.empty}",groups = PhoneValidGroups.NotBlankCheck.class)
    @Length(max = 15,message = "{error.field.phone.size}",groups = PhoneValidGroups.NotLength.class)
    private String phone;
    private String status;

    @Override
    public void updateEntity(Call entity, CallMapper mapper) {
        mapper.toEntityForSaveCall(this,entity);
    }
}
