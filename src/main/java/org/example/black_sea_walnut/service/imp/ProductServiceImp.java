package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.*;
import org.example.black_sea_walnut.dto.historyPrice.HistoryResponsePricesForProduct;
import org.example.black_sea_walnut.dto.product.ProductRequestForAdd;
import org.example.black_sea_walnut.dto.product.ProductResponseForAdd;
import org.example.black_sea_walnut.dto.product.ProductResponseForShopPage;
import org.example.black_sea_walnut.dto.product.ProductResponseForViewInProducts;
import org.example.black_sea_walnut.dto.web.ProductResponseForViewInTable;
import org.example.black_sea_walnut.entity.HistoryPrices;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.mapper.ProductMapper;
import org.example.black_sea_walnut.repository.ProductRepository;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.service.HistoryPricesService;
import org.example.black_sea_walnut.service.ProductService;
import org.example.black_sea_walnut.service.TasteService;
import org.example.black_sea_walnut.service.specifications.ProductSpecification;
import org.example.black_sea_walnut.service.specifications.ProductSpecification2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final TasteService tasteService;
    private final DiscountService discountService;
    private final ImageServiceImp imageServiceImp;
    private final ProductMapper productMapper;
    private final HistoryPricesService historyPricesService;


    @Value("${upload.path}")
    private String contextPath;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public PageResponse<ProductResponseForViewInProducts> getAll(ProductResponseForViewInProducts response, Pageable pageable, LanguageCode code) {
        Page<Product> page = productRepository.findAll(ProductSpecification.getSpecification(response, code), pageable);
        List<ProductResponseForViewInProducts> responseDTOView = page.map(p -> mapper.toDTOForView(p, code)).stream().toList();
        return new PageResponse<>(responseDTOView, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public PageResponse<ProductResponseForViewInTable> getAll(ProductResponseForShopPage response, Pageable pageable, LanguageCode code) {
        Page<Product> page = productRepository.findAll(ProductSpecification2.getSpecification(response, code), pageable);
        List<ProductResponseForViewInTable> responseDTOView = page.map(p -> mapper.toResponseForViewInProduction(p, code)).toList();
        return new PageResponse<>(responseDTOView, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public Product save(Product entity) {
        return productRepository.save(entity);
    }

    @SneakyThrows
    @Override
    public Product save(ProductRequestForAdd dto) {
        if (dto.getId() != null) {
            Product product = getById(dto.getId());
            if (dto.getPathToImage1().isEmpty())
                imageServiceImp.deleteByPath(product.getPathToImage1());
            if (dto.getPathToImage2().isEmpty())
                imageServiceImp.deleteByPath(product.getPathToImage2());
            if (dto.getPathToImage3().isEmpty())
                imageServiceImp.deleteByPath(product.getPathToImage3());
            if (dto.getPathToImage4().isEmpty())
                imageServiceImp.deleteByPath(product.getPathToImage4());
            if (dto.getPathToImageDelivery().isEmpty())
                imageServiceImp.deleteByPath(product.getPathToImageDelivery());
            if (dto.getPathToImageDescription().isEmpty())
                imageServiceImp.deleteByPath(product.getPathToImageDescription());
            if (dto.getPathToImagePacking().isEmpty())
                imageServiceImp.deleteByPath(product.getPathToImagePacking());
            if (dto.getPathToImagePayment().isEmpty())
                imageServiceImp.deleteByPath(product.getPathToImagePayment());
            if (dto.getNewPrice() != null) {
                product.getHistoryPrices().add(new HistoryPrices(dto.getNewPrice(), LocalDateTime.now(), LocalDateTime.now().plusDays(30), product));
                save(product);
            } else {
                dto.setNewPrice(dto.getCurrentPrice());
            }
        }
        if (dto.getImage1() != null) {
            String generatedPath = contextPath + "/products/" + MediaType.image + "/" + imageServiceImp.generateFileName(dto.getImage1());
            dto.setPathToImage1(generatedPath);
        }
        if (dto.getImage2() != null) {
            String generatedPath = contextPath + "/products/" + MediaType.image + "/" + imageServiceImp.generateFileName(dto.getImage2());
            dto.setPathToImage2(generatedPath);
        }
        if (dto.getImage3() != null) {
            String generatedPath = contextPath + "/products/" + MediaType.image + "/" + imageServiceImp.generateFileName(dto.getImage3());
            dto.setPathToImage3(generatedPath);
        }
        if (dto.getImage4() != null) {
            String generatedPath = contextPath + "/products/" + MediaType.image + "/" + imageServiceImp.generateFileName(dto.getImage4());
            dto.setPathToImage4(generatedPath);
        }
        if (dto.getImageDescription() != null) {
            String generatedPath = contextPath + "/products/" + MediaType.image + "/" + imageServiceImp.generateFileName(dto.getImageDescription());
            dto.setPathToImageDescription(generatedPath);
        }
        if (dto.getImagePacking() != null) {
            String generatedPath = contextPath + "/products/" + MediaType.image + "/" + imageServiceImp.generateFileName(dto.getImagePacking());
            dto.setPathToImagePacking(generatedPath);
        }
        if (dto.getImagePayment() != null) {
            String generatedPath = contextPath + "/products/" + MediaType.image + "/" + imageServiceImp.generateFileName(dto.getImagePayment());
            dto.setPathToImagePayment(generatedPath);
        }
        if (dto.getImageDelivery() != null) {
            String generatedPath = contextPath + "/products/" + MediaType.image + "/" + imageServiceImp.generateFileName(dto.getImageDelivery());
            dto.setPathToImageDelivery(generatedPath);
        }

        Product product = mapper.toEntityForRequestAdd(dto);
        discountService.getAllByDiscountCommonId(dto.getDiscountId()).stream()
                .findFirst().ifPresent(discount -> product.setDiscounts(Set.of(discount)));
        tasteService.getAllByCommonId(dto.getTasteId()).stream()
                .findFirst().ifPresent(taste -> product.setTastes(Set.of(taste)));
        Product productSaved = save(product);

        imageServiceImp.save(dto.getImage1(), productSaved.getPathToImage1());
        imageServiceImp.save(dto.getImage2(), productSaved.getPathToImage2());
        imageServiceImp.save(dto.getImage3(), productSaved.getPathToImage3());
        imageServiceImp.save(dto.getImage4(), productSaved.getPathToImage4());
        imageServiceImp.save(dto.getImageDescription(), productSaved.getPathToImageDescription());
        imageServiceImp.save(dto.getImageDelivery(), productSaved.getPathToImageDelivery());
        imageServiceImp.save(dto.getImagePacking(), productSaved.getPathToImagePacking());
        imageServiceImp.save(dto.getImagePayment(), productSaved.getPathToImagePayment());
        return productSaved;
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product with id: " + id + " was not found!")
        );
    }

    @Override
    public ProductResponseForAdd getByIdLikeDTOAdd(Long id) {
        Product product = getById(id);
        ProductResponseForAdd productInDtoAdd = mapper.toResponseForAdd(product);
        HistoryResponsePricesForProduct responseHistoryPrices = historyPricesService.getLatestPriceByProductIdInDtoForProduct(product.getId());
        productInDtoAdd.setPrices(responseHistoryPrices);
        return productInDtoAdd;
    }

    @Override
    public void deleteById(Long id) throws IOException {
        Product product = getById(id);
        if (product.getPathToImage1() != null && product.getPathToImage1().isEmpty())
            imageServiceImp.deleteByPath(product.getPathToImage1());
        if (product.getPathToImage2() != null && product.getPathToImage2().isEmpty())
            imageServiceImp.deleteByPath(product.getPathToImage2());
        if (product.getPathToImage3() != null && product.getPathToImage3().isEmpty())
            imageServiceImp.deleteByPath(product.getPathToImage3());
        if (product.getPathToImage4() != null && product.getPathToImage4().isEmpty())
            imageServiceImp.deleteByPath(product.getPathToImage4());
        if (product.getPathToImageDelivery() != null && product.getPathToImageDelivery().isEmpty())
            imageServiceImp.deleteByPath(product.getPathToImageDelivery());
        if (product.getPathToImageDescription() != null && product.getPathToImageDescription().isEmpty())
            imageServiceImp.deleteByPath(product.getPathToImageDescription());
        if (product.getPathToImagePacking() != null && product.getPathToImagePacking().isEmpty())
            imageServiceImp.deleteByPath(product.getPathToImagePacking());
        if (product.getPathToImagePayment() != null && product.getPathToImagePayment().isEmpty())
            imageServiceImp.deleteByPath(product.getPathToImagePayment());
        productRepository.deleteById(id);
    }

    @Override
    public boolean isExistByArticleId(Long id) {
        return productRepository.existsByArticleId(id);
    }

    @Override
    public boolean isExistById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public List<ProductResponseForViewInTable> getRandomProductsBySize(int size, LanguageCode code) {
        return productRepository.findRandomProducts(size).stream().map(p -> {
            p.setHistoryPrices(historyPricesService.getLastTwoDataByProduct(p));
            return productMapper.toResponseForViewInMain(p, code);
        }).toList();
    }

    @Override
    public List<Integer> getAllMasses() {
        return productRepository.getAllMasses();
    }
}