package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.repository.HistoryRepository;
import org.example.black_sea_walnut.service.HistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImp implements HistoryService {
    private final HistoryRepository historyRepository;

    @Override
    public History getById(Long id) {
        return historyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("History with id:" + id + " was not found!"));
    }

    @Override
    public History getByPageType(PageType type) {
        return historyRepository.findByPageType(type).orElseThrow(() -> new EntityNotFoundException("History with type: " + type + " was not found!"));
    }

    @Override
    public History save(History entity) {
        return historyRepository.save(entity);
    }

    @Override
    public List<History> getAll() {
        return historyRepository.findAll();
    }
}
