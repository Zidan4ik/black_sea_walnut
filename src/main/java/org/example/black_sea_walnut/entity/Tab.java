package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.TabTranslation;

import java.util.List;

@Entity
@Table(name = "tabs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    private String pathToImage;
    private String pathToIcon;
    @OneToMany
    private List<TabTranslation> translations;
}
