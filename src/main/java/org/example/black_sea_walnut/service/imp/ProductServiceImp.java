package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.*;
import org.example.black_sea_walnut.dto.admin.historyPrice.HistoryResponsePricesForProduct;
import org.example.black_sea_walnut.dto.admin.product.ProductRequestForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForAdd;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForShopPage;
import org.example.black_sea_walnut.dto.admin.product.ProductResponseForViewInProducts;
import org.example.black_sea_walnut.dto.web.ProductResponseForViewInTable;
import org.example.black_sea_walnut.entity.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    @Transactional
    @Override
    public Product save(ProductRequestForAdd dto) {
        Product product;

        if (dto.getId() != null) {
            product = getById(dto.getId());

            processImage(dto.getImage1(), dto.getPathToImage1(), product::setPathToImage1);
            processImage(dto.getImage2(), dto.getPathToImage2(), product::setPathToImage2);
            processImage(dto.getImage3(), dto.getPathToImage3(), product::setPathToImage3);
            processImage(dto.getImage4(), dto.getPathToImage4(), product::setPathToImage4);
            processImage(dto.getImageDescription(), dto.getPathToImageDescription(), product::setPathToImageDescription);
            processImage(dto.getImagePacking(), dto.getPathToImagePacking(), product::setPathToImagePacking);
            processImage(dto.getImagePayment(), dto.getPathToImagePayment(), product::setPathToImagePayment);
            processImage(dto.getImageDelivery(), dto.getPathToImageDelivery(), product::setPathToImageDelivery);

            if (dto.getNewPrice() != null) {
                product.getHistoryPrices().add(new HistoryPrices(dto.getNewPrice(), LocalDateTime.now(), LocalDateTime.now().plusDays(30), product));
            } else {
                dto.setNewPrice(dto.getCurrentPrice());
            }
        } else {
            product = mapper.toEntityForRequestAdd(dto);
        }

        product.setDiscounts(new HashSet<>(discountService.getAllByDiscountCommonId(dto.getDiscountId())
                .stream().filter(d -> d.getLanguageCode().equals(LanguageCode.en)).collect(Collectors.toSet())));
        product.setTastes(new HashSet<>(tasteService.getAllByCommonId(dto.getTasteId())
                .stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).collect(Collectors.toSet())));

        Product productSaved = save(product);

        imageServiceImp.save(dto.getImage1(), productSaved.getPathToImage1());
        imageServiceImp.save(dto.getImage2(), productSaved.getPathToImage2());
        imageServiceImp.save(dto.getImage3(), productSaved.getPathToImage3());
        imageServiceImp.save(dto.getImage4(), productSaved.getPathToImage4());
        imageServiceImp.save(dto.getImageDescription(), productSaved.getPathToImageDescription());
        imageServiceImp.save(dto.getImagePacking(), productSaved.getPathToImagePacking());
        imageServiceImp.save(dto.getImagePayment(), productSaved.getPathToImagePayment());
        imageServiceImp.save(dto.getImageDelivery(), productSaved.getPathToImageDelivery());

        return productSaved;
    }

    @SneakyThrows
    private void processImage(MultipartFile image, String imagePath, Consumer<String> pathSetter) {
        if (image != null && !image.isEmpty()) {
            String generatedPath = contextPath + "/products/" + MediaType.image + "/" + imageServiceImp.generateFileName(image);
            pathSetter.accept(generatedPath);
        } else if (imagePath != null && !imagePath.isEmpty()) {
            pathSetter.accept(null);
            imageServiceImp.deleteByPath(imagePath);
        }
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

    @Override
    public void decreaseCountItems(Long productId) {
        Product product = getById(productId);
        if (product.getTotalCount() > 0) {
            product.setTotalCount(product.getTotalCount() - 1);
        }
    }

    @Override
    public void increaseCountItems(Long productId) {
        Product product = getById(productId);
        product.setTotalCount(product.getTotalCount() + 1);
    }
}