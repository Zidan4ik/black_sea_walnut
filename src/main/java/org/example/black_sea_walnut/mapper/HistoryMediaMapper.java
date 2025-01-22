package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.springframework.stereotype.Component;

@Component
public class HistoryMediaMapper {
    public HistoryMediaResponseForAdd toResponseForAdd(HistoryMedia entity) {
        return HistoryMediaResponseForAdd
                .builder()
                .id(entity.getId())
                .pathToImage(entity.getPathToImage())
                .build();
    }

    public HistoryMedia toEntityFromRequestForAdd(HistoryMediaRequestForAdd dto, History foreignTable) {
        HistoryMedia entity = new HistoryMedia();
        entity.setId(dto.getId());
        entity.setMediaType(dto.getMediaType());
        entity.setPathToImage(dto.getPathToImage());
        entity.setHistory(foreignTable);
        return entity;
    }
}
