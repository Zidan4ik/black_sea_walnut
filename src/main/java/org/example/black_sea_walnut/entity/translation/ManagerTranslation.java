package org.example.black_sea_walnut.entity.translation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.Manager;
import org.example.black_sea_walnut.enums.LanguageCode;

@Entity
@Table(name = "managers_translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManagerTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
    private String name;
    private String surname;
    @ManyToOne
    public Manager manager;

    public ManagerTranslation(LanguageCode languageCode, String name, String surname, Manager manager) {
        this.languageCode = languageCode;
        this.name = name;
        this.surname = surname;
        this.manager = manager;
    }
}
