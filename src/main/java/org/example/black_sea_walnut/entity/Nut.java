package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.NutTranslation;
import org.example.black_sea_walnut.service.file.ImageEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nuts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Nut implements ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    private String pathToImage;
    private String pathToSvg;
    private LocalDate date;
    @OneToMany(mappedBy = "nut", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutTranslation> translations = new ArrayList<>();
}
