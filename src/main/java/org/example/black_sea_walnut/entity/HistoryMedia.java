package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;

@Entity
@Table(name = "histories_medias")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoryMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MediaType mediaType;
    private String pathToImage;
    @ManyToOne
    @JoinColumn(name = "history_id")
    public History history;
}
