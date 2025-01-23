package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.entity.translation.HistoryTranslation;
import org.example.black_sea_walnut.enums.PageType;

import java.util.ArrayList;
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
    @Column(unique = true)
    private PageType pageType;
    @OneToOne(mappedBy = "history", cascade = CascadeType.ALL)
    public Banner banner;
    @OneToMany(mappedBy = "history",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HistoryTranslation> translations = new ArrayList<>();
    @OneToMany(mappedBy = "history",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<HistoryMedia> historyMedia = new ArrayList<>();
}
