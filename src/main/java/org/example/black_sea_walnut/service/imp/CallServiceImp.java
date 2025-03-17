package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.calls.CallResponseForView;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.mapper.CallMapper;
import org.example.black_sea_walnut.repository.CallRepository;
import org.example.black_sea_walnut.service.CallService;
import org.example.black_sea_walnut.service.specifications.CallSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CallServiceImp implements CallService {
    private final CallRepository callRepository;
    private final CallMapper callMapper;

    @Override
    public List<Call> getAll() {
        return callRepository.findAll();
    }

    @Override
    public PageResponse<CallResponseForView> getAll(CallResponseForView response, Pageable pageable) {
        Page<Call> page = callRepository.findAll(CallSpecification.getSpecification(response), pageable);
        List<CallResponseForView> responsesDtoAdd = page.map(callMapper::toResponseForView).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public void deleteById(Long id) {
        callRepository.deleteById(id);
    }

    @Override
    public void save(CallResponseForView dto) {
        callRepository.save(callMapper.toEntityForSaveCall(dto));
    }
}
