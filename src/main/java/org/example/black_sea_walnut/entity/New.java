package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.NewTranslation;
import org.example.black_sea_walnut.enums.MediaType;

import java.time.LocalDate;
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
    private LocalDate dateOfPublication;
    private String pathToMedia;
    private MediaType mediaType;
    @OneToMany(mappedBy = "new_",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<NewTranslation> translations;
}