package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.NutTranslation;

import java.util.List;

@Entity
@Table(name = "nuts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Nut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    private String pathToImage;
    private String pathToSvg;
    @OneToMany
    private List<NutTranslation> translations;
}