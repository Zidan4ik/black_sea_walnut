package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.repository.CallRepository;
import org.example.black_sea_walnut.service.CallService;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CallServiceImp implements CallService {
    private final CallRepository callRepository;

    @Override
    public List<Call> getAll() {
        LogUtil.logInfo("Fetching all calls from the repository.");
        List<Call> calls = callRepository.findAll();
        LogUtil.logInfo("Fetched " + calls.size() + " calls.");
        return calls;
    }

    @Override
    @Transactional(readOnly = true)
    public <R> PageResponse<R> getAll(Specification<Call> spec, Pageable pageable, Function<Call,R> mappingFunction) {
        Page<Call> page = callRepository.findAll(spec, pageable);
        List<R> content = page.map(mappingFunction).getContent();
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Attempting to delete call with ID: " + id);
        callRepository.deleteById(id);
        LogUtil.logInfo("Call with ID " + id + " was deleted successfully.");
    }

    @Override
    public <M extends GenericsMapper> Call save(Saveable<Call, M> dto, M mapper) {
        LogUtil.logInfo("Saving call with details: " + dto.toString());
        Call entity = findOrCreate(dto.getId());
        dto.updateEntity(entity, mapper);
        return callRepository.save(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        LogUtil.logInfo("Attempting to delete call with Ids: " + ids);
        callRepository.deleteAllById(ids);
        LogUtil.logInfo("Call with Ids" + ids + " was deleted successfully.");
    }

    @Override
    public Call getById(Long id) {
        LogUtil.logInfo("Fetching Call by ID: " + id);
        return callRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "Call with id: " + id + " was not found!";
                    LogUtil.logError(errorMessage, null);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    private Call findOrCreate(Long id) {
        return (id != null) ? getById(id) : new Call();
    }
}
