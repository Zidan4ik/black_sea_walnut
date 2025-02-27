package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.calls.CallResponseForView;
import org.example.black_sea_walnut.dto.gallery.GalleryRequestForAdd;
import org.example.black_sea_walnut.dto.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.GalleryMapper;
import org.example.black_sea_walnut.repository.GalleryRepository;
import org.example.black_sea_walnut.service.GalleryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.service.specifications.CallSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GalleryServiceImp implements GalleryService {
    private final GalleryRepository galleryRepository;
    private final GalleryMapper mapper;
    private final ImageService imageService;

    @Value("${upload.path}")
    private String contextPath;

    @Override
    public Gallery save(Gallery gallery) {
        return galleryRepository.save(gallery);
    }

    @SneakyThrows
    @Override
    public Gallery save(GalleryRequestForAdd dto) {
        if (dto.getId() != null) {
            Gallery galleryById = getById(dto.getId());
            if (dto.getPathToMediaFile().isEmpty())
                imageService.deleteByPath(galleryById.getPathToMedia());
        }

        if (dto.getFile() != null) {
            String generatedPath = contextPath + "/gallery/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getFile());
            dto.setPathToMediaFile(generatedPath);
        }


        Gallery gallery = save(mapper.toEntityForRequestAdd(dto));
        imageService.save(dto.getFile(), gallery.getPathToMedia());
        return gallery;
    }

    @Override
    public List<Gallery> getAll() {
        return galleryRepository.findAll();
    }

    @Override
    public List<Gallery> getAllByLanguageCode(LanguageCode code) {
        return galleryRepository.findAllByLanguageCode(code);
    }

    @Override
    public List<GalleryResponseForAdd> getAllInResponseByLanguageCode(LanguageCode languageCoed) {
        return getAllByLanguageCode(languageCoed).stream().map(mapper::toResponseForAdd).toList();
    }

    @Override
    public PageResponse<GalleryResponseForAdd> getAllInResponseByLanguageCode(Pageable pageable, LanguageCode code) {
        Specification<Gallery> spec = Specification.where(((root, query, criteriaBuilder) -> {
            Join<Object, Object> translations = root.join("translations");
            return criteriaBuilder.equal(translations.get("languageCode"), code);
        }));
        Page<Gallery> page = galleryRepository.findAll(spec,pageable);
        List<GalleryResponseForAdd> responsesDtoAdd = page.map(mapper::toResponseForAdd).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        Gallery galleryById = getById(id);
        if (galleryById.getPathToMedia() != null)
            imageService.deleteByPath(galleryById.getPathToMedia());
        galleryRepository.deleteById(id);
    }

    @Override
    public Gallery getById(Long id) {
        return galleryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Gallery with id: " + id + " was not found!")
        );
    }
}
