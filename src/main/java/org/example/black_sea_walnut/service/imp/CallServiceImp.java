package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.calls.CallResponseForView;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.mapper.CallMapper;
import org.example.black_sea_walnut.repository.CallRepository;
import org.example.black_sea_walnut.service.CallService;
import org.example.black_sea_walnut.service.specifications.CallSpecification;
import org.example.black_sea_walnut.util.LogUtil;
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
        LogUtil.logInfo("Fetching all calls from the repository.");
        List<Call> calls = callRepository.findAll();
        LogUtil.logInfo("Fetched " + calls.size() + " calls.");
        return calls;
    }

    @Override
    public PageResponse<CallResponseForView> getAll(CallResponseForView response, Pageable pageable) {
        LogUtil.logInfo("Fetching paginated calls with filters: " + response.toString());
        Page<Call> page = callRepository.findAll(CallSpecification.getSpecification(response), pageable);
        List<CallResponseForView> responsesDtoAdd = page.map(callMapper::toResponseForView).stream().toList();
        LogUtil.logInfo("Fetched " + responsesDtoAdd.size() + " calls in page " + page.getNumber());
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
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
    public void save(CallResponseForView dto) {
        LogUtil.logInfo("Saving call with details: " + dto.toString());
        callRepository.save(callMapper.toEntityForSaveCall(dto));
        LogUtil.logInfo("Call with details " + dto.toString() + " has been saved.");
    }
}
