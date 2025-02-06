package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.calls.CallResponseForView;
import org.example.black_sea_walnut.entity.Call;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CallService {
    List<Call> getAll();

    PageResponse<CallResponseForView> getAll(CallResponseForView response, Pageable pageable);

    void deleteById(Long id);
}
