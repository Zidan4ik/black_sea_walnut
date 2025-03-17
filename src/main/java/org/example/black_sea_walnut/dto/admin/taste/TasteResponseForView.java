package org.example.black_sea_walnut.dto.admin.taste;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TasteResponseForView {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    private String name;
}
