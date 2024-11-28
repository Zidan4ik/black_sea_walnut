package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.NewTranslation;
import org.example.black_sea_walnut.enums.MediaType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "news")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class New {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    private LocalDateTime timeUpdate;
    private String pathToMedia;
    private MediaType mediaType;
    @OneToMany
    private List<NewTranslation> translations;
}