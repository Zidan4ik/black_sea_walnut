package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.GalleryTranslation;
import org.example.black_sea_walnut.enums.MediaType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "galleries")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MediaType mediaType;
    private boolean isActive;
    private LocalDateTime timeUpdate;
    private String pathToMedia;
    @OneToMany
    private List<GalleryTranslation> translations;
}
