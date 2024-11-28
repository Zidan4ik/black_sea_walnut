package org.example.black_sea_walnut.entity.translation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.LanguageCode;

@Entity
@Table(name = "managers_translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManagerTranslations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LanguageCode languageCode;
    private String name;
    private String surname;
}
