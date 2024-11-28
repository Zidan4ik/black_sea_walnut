package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.HistoryTranslation;

import java.util.List;

@Entity
@Table(name = "histories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    @OneToMany
    private List<HistoryTranslation> translations;
    @OneToMany
    private List<HistoryMedia> historyMedia;
}
