package org.example.black_sea_walnut.entity.translation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;

@Entity
@Table(name = "news_translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LanguageCode languageCode;
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "new_id")
    private New new_;

    public NewTranslation(LanguageCode languageCode, String title, String description, New new_) {
        this.languageCode = languageCode;
        this.title = title;
        this.description = description;
        this.new_ = new_;
    }
}
