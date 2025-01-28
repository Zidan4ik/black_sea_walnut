package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.black_sea_walnut.entity.translation.ClientCategoryTranslation;
import org.example.black_sea_walnut.enums.MediaType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client_category")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    private String pathToImage;
    private String pathToSvg;
    private MediaType mediaTypeSvg;
    private MediaType mediaTypeImage;
    @OneToMany(mappedBy = "clientCategory",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ClientCategoryTranslation> translations = new ArrayList<>();
}
