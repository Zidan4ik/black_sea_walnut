package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;


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
    private String pathToMedia;
    private MediaType mediaType;
    @OneToOne
    @JoinColumn(name = "history_id")
    public History history;
}
