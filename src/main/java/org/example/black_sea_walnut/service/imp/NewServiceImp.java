package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.ResponseNewForAdd;
import org.example.black_sea_walnut.dto.ResponseNewForView;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.NewMapper;
import org.example.black_sea_walnut.repository.NewRepository;
import org.example.black_sea_walnut.service.NewService;
import org.example.black_sea_walnut.service.specifications.NewSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewServiceImp implements NewService {
    private final NewRepository newRepository;
    private final ImageServiceImp imageServiceImp;
    private final NewMapper mapper = new NewMapper();
    @Value("${upload.path}")
    private String contextPath;

    @Override
    public PageResponse<ResponseNewForView> getAll(ResponseNewForView response, Pageable pageable, LanguageCode code) {
        Page<New> page = newRepository.findAll(NewSpecification.getSpecification(response), pageable);
        List<ResponseNewForView> responsesDtoAdd = page.map(t -> mapper.toDtoView(t, code)).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public New getById(Long id) {
        return newRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("New with id: " + id + " was not found!")
        );
    }

    @Override
    public ResponseNewForAdd getByIdAndCodeInResponseAdd(Long id, LanguageCode code) {
        return mapper.toDtoAdd(getById(id), code);
    }

    @Override
    public List<New> getAll() {
        return newRepository.findAll();
    }

    @Override
    @Transactional
    public New save(New entity) {
        return newRepository.save(entity);
    }

    @Override
    public New saveAsDto(ResponseNewForAdd dto) {
        return save(mapper.toEntity(dto));
    }

    @Override
    public New saveImage(ResponseNewForAdd dto) throws IOException {
//        LogUtil.logInfo("Saving new_ with file for ID: " + dtoAdd.getId());
        if (dto.getId() != null) {
            New newById = getById(dto.getId());
            if (dto.getFile() != null && (newById.getPathToMedia() != null
                    && !newById.getPathToMedia().equals(dto.getPathToImage()))) {
//                LogUtil.logInfo("Deleting old image at path: " + newById.getPathToImage());
                imageServiceImp.deleteByPath(newById.getPathToMedia());
            }
        }

        if (dto.getFile() != null) {
            String generatedPath = contextPath + "/service/" + dto.getMediaType().toString() + "/" + imageServiceImp.generateFileName(dto.getFile());
            dto.setPathToImage(generatedPath);
//            LogUtil.logInfo("Generated new path for image: " + generatedPath);
        }
        New new_ = saveAsDto(dto);
        imageServiceImp.save(dto.getFile(), new_.getPathToMedia());
//        LogUtil.logInfo("Saved new_ with id: " + new_.getId() + " - " + new_);
        return new_;
    }

    @Override
    public void deleteById(Long id) {
        newRepository.deleteById(id);
    }
}
