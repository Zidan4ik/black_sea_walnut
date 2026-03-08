package org.example.black_sea_walnut.service.history;

import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.user.Saveable;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public interface HistoryService {
    History getById(Long id);

    History getByPageType(PageType type);

    <R extends HistoryResponse> R getResponseByPageType(PageType type, Function<History, R> mappingFunction);

    History save(History entity);

    List<History> getAll();

    boolean isNewImageProvided(FileProcessable dto);

    void safeDelete(String path);

    void handleFileSynchronization(HistoryFileRequest dto, History entity, Uploadable sub);

    void handleBannerUpdate(History entity, FileProcessable fileDto, Uploadable u) throws IOException;

    History getOrCreate(Long id);

    <M extends GenericsMapper> History saveHistory(Saveable<History, M> dto, M mapper);
}
