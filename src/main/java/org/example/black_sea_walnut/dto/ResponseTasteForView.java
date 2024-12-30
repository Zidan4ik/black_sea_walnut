package org.example.black_sea_walnut.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseTasteForView {
    private Long id;
    private String name;
}
