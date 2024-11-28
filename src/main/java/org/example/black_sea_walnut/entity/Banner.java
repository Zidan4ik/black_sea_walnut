package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.BannerTranslation;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.enums.PageType;

import java.util.List;

@Entity
@Table(name = "banners")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    private PageType pageType;
    private String pathToMedia;
    private MediaType mediaType;
    private boolean isUp;
    @OneToMany
    private List<BannerTranslation> translations;
}
