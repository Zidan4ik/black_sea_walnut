package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.GalleryMapper;
import org.example.black_sea_walnut.repository.GalleryRepository;
import org.example.black_sea_walnut.service.GalleryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.util.LogUtil;
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
        LogUtil.logInfo("Saving gallery: " + gallery);
        Gallery savedGallery = galleryRepository.save(gallery);
        LogUtil.logInfo("Gallery saved successfully: " + savedGallery);
        return savedGallery;
    }

    @SneakyThrows
    @Override
    public Gallery save(GalleryRequestForAdd dto) {
        LogUtil.logInfo("Saving gallery from DTO: " + dto);
        if (dto.getId() != null) {
            Gallery galleryById = getById(dto.getId());
            if (dto.getPathToMediaFile().isEmpty()) {
                LogUtil.logInfo("Deleting media file for gallery with ID: " + dto.getId());
                imageService.deleteByPath(galleryById.getPathToMedia());
            }
        }

        if (dto.getFile() != null) {
            String generatedPath = contextPath + "/gallery/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getFile());
            dto.setPathToMediaFile(generatedPath);
            LogUtil.logInfo("Generated media file path: " + generatedPath);
        }

        Gallery gallery = save(mapper.toEntityForRequestAdd(dto));
        imageService.save(dto.getFile(), gallery.getPathToMedia());
        LogUtil.logInfo("Gallery saved successfully with media: " + gallery);
        return gallery;
    }

    @Override
    public List<Gallery> getAll() {
        LogUtil.logInfo("Fetching all galleries.");
        List<Gallery> galleries = galleryRepository.findAll();
        LogUtil.logInfo("Fetched " + galleries.size() + " galleries.");
        return galleries;
    }

    @Override
    public List<Gallery> getAllByLanguageCode(LanguageCode code) {
        LogUtil.logInfo("Fetching galleries for language code: " + code);
        List<Gallery> galleries = galleryRepository.findAllByLanguageCode(code);
        LogUtil.logInfo("Fetched " + galleries.size() + " galleries for language code: " + code);
        return galleries;
    }

    @Override
    public List<GalleryResponseForAdd> getAllInResponseByLanguageCode(LanguageCode languageCode) {
        LogUtil.logInfo("Fetching gallery responses for add with language code: " + languageCode);
        List<GalleryResponseForAdd> responses = getAllByLanguageCode(languageCode).stream()
                .map(mapper::toResponseForAdd)
                .toList();
        LogUtil.logInfo("Fetched " + responses.size() + " gallery responses for language code: " + languageCode);
        return responses;
    }

    @Override
    public PageResponse<GalleryResponseForAdd> getAllInResponseByLanguageCode(Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching paginated gallery responses for add with language code: " + code);
        Specification<Gallery> spec = Specification.where((root, query, criteriaBuilder) -> {
            Join<Object, Object> translations = root.join("translations");
            return criteriaBuilder.equal(translations.get("languageCode"), code);
        });
        Page<Gallery> page = galleryRepository.findAll(spec, pageable);
        List<GalleryResponseForAdd> responsesDtoAdd = page.map(mapper::toResponseForAdd).stream().toList();
        LogUtil.logInfo("Fetched " + responsesDtoAdd.size() + " gallery responses for language code: " + code);
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting gallery with ID: " + id);
        Gallery galleryById = getById(id);
        if (galleryById.getPathToMedia() != null) {
            LogUtil.logInfo("Deleting media file for gallery with ID: " + id);
            imageService.deleteByPath(galleryById.getPathToMedia());
        }
        galleryRepository.deleteById(id);
        LogUtil.logInfo("Gallery with ID: " + id + " deleted successfully.");
    }

    @Override
    public Gallery getById(Long id) {
        LogUtil.logInfo("Fetching gallery with ID: " + id);
        Gallery gallery = galleryRepository.findById(id).orElseThrow(
                () -> {
                    LogUtil.logError("Gallery with ID: " + id + " was not found!", null);
                    return new EntityNotFoundException("Gallery with id: " + id + " was not found!");
                });
        LogUtil.logInfo("Gallery with ID: " + id + " found successfully.");
        return gallery;
    }
}
