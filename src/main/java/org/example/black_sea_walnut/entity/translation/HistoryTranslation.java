package org.example.black_sea_walnut.entity.translation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.History;
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
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
    private String title;
    private String title2;
    private String title3;
    private String title4;
    private String subtitle;
    private String description;
    private String description2;
    private String description3;
    private String description4;
    @ManyToOne
    @JoinColumn(name = "history_id")
    public History history;

    public HistoryTranslation(LanguageCode languageCode, String title, String description, History history) {
        this.languageCode = languageCode;
        this.title = title;
        this.description = description;
        this.history = history;
    }


    public HistoryTranslation(LanguageCode languageCode, String title, String title2, String title3, String title4, String description, String description2, String description3, String description4, History history) {
        this.languageCode = languageCode;
        this.title = title;
        this.title2 = title2;
        this.title3 = title3;
        this.title4 = title4;
        this.description = description;
        this.description2 = description2;
        this.description3 = description3;
        this.description4 = description4;
        this.history = history;
    }
}
