package org.example.black_sea_walnut.service.history;

import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.mapper.HistoryMediaMapper;

import java.util.List;

public interface GenericsMapper {

    default void updateHistoryMedia(
            History entity,
            List<HistoryMediaRequestForAdd> mediaDtos,
            HistoryMediaMapper mediaMapper) {
        entity.getHistoryMedia().clear();
        if(mediaDtos!=null){
            entity.getHistoryMedia().addAll(
                    mediaDtos.stream()
                            .map(d->mediaMapper.toEntityFromRequestForAdd(d,entity)).toList()
            );
        }
    }
}
