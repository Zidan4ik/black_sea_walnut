package org.example.black_sea_walnut.entity.translation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.LanguageCode;

@Entity
@Table(name = "history_translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoryTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LanguageCode languageCode;
    private String title;
    private String subtitle;
    private String description;
}
