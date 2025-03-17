package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.admin.calls.CallResponseForView;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.enums.CallStatus;
import org.example.black_sea_walnut.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CallMapper {
    public CallResponseForView toResponseForView(Call entity) {
        LocalDateTime registerCall = entity.getRegisterCall();
        String time = registerCall.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return CallResponseForView
                .builder()
                .id(entity.getId())
                .phone(entity.getPhone())
                .date(DateUtil.toFormatDateFromDB(registerCall.toLocalDate(), "dd.MM.yyyy"))
                .time(time)
                .status(entity.getStatus().toString())
                .build();
    }

    public Call toEntityForSaveCall(CallResponseForView dto) {
        Call entity = new Call();
        entity.setRegisterCall(LocalDateTime.now());
        entity.setPhone(dto.getPhone());
        entity.setStatus(CallStatus.new_);
        return entity;
    }
}
