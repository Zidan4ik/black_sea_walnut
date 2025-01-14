package org.example.black_sea_walnut.entity.translation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.enums.LanguageCode;

@Entity
@Table(name = "galleries_translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GalleryTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
    private String title;
    @Lob
    private String description;
    @ManyToOne
    private Gallery gallery;
}
