package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;

import java.util.List;

public interface HistoryService {
    History getById(Long id);
    History getByPageType(PageType type);
    History save(History entity);
    List<History> getAll();
}
