package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.LanguageCode;

@Entity
@Table(name = "tastes",uniqueConstraints = @UniqueConstraint(columnNames = {"tasteId","languageCode"}))
@Getter
@Setter
public class Taste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tasteId;
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
    private String name;
}
