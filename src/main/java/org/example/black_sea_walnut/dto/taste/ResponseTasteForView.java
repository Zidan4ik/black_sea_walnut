package org.example.black_sea_walnut.dto.taste;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseTasteForView {
    private Long id;
    private String name;
}
