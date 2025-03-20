package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.repository.HistoryRepository;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImp implements HistoryService {
    private final HistoryRepository historyRepository;

    @Override
    public History getById(Long id) {
        LogUtil.logInfo("Fetching history by ID: " + id);
        return historyRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "History with id: " + id + " was not found!";
                    LogUtil.logError(errorMessage, null);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    @Override
    public History getByPageType(PageType type) {
        LogUtil.logInfo("Fetching history by PageType: " + type);
        return historyRepository.findByPageType(type)
                .orElseThrow(() -> {
                    String errorMessage = "History with type: " + type + " was not found!";
                    LogUtil.logError(errorMessage, null);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    @Override
    public History save(History entity) {
        LogUtil.logInfo("Saving history: " + entity);
        return historyRepository.save(entity);
    }

    @Override
    public List<History> getAll() {
        LogUtil.logInfo("Fetching all history records");
        return historyRepository.findAll();
    }
}
