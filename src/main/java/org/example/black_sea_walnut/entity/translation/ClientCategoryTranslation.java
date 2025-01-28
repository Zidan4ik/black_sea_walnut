package org.example.black_sea_walnut.entity.translation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.ClientCategory;
import org.example.black_sea_walnut.enums.LanguageCode;

@Entity
@Table(name = "client_category_translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientCategoryTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
    private String title;
    private String subtitle;
    private String description;
    @ManyToOne
    @JoinColumn(name = "clientCategory_id")
    public ClientCategory clientCategory;
}
