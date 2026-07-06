package org.example.black_sea_walnut.dto.admin.taste;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.service.Nameable;
import org.example.black_sea_walnut.service.history.DtoResponse;

@Builder
@Getter
public class TasteResponseForView implements DtoResponse, Nameable {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    private String name;
}
