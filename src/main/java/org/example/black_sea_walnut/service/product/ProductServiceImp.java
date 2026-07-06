package org.example.black_sea_walnut.service.product;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.*;
import org.example.black_sea_walnut.dto.web.ProductResponseForViewInTable;
import org.example.black_sea_walnut.entity.*;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.ProductMapper;
import org.example.black_sea_walnut.repository.ProductRepository;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.service.HistoryPricesService;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.imp.ImageServiceImp;
import org.example.black_sea_walnut.service.imp.OrderDetailServiceImp;
import org.example.black_sea_walnut.service.product.taste.TasteService;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.util.ImageUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;
    private final TasteService tasteService;
    private final DiscountService discountService;
    private final ImageServiceImp imageServiceImp;
    private final ProductMapper productMapper;
    private final HistoryPricesService historyPricesService;
    private final OrderDetailServiceImp orderDetailService;

    @Override
    public List<Product> getAll() {
        LogUtil.logInfo("Fetching all products");
        List<Product> products = productRepository.findAll();
        LogUtil.logInfo("Fetched product: " + products.size());
        return products;
    }

    @Override
    public <R> PageResponse<R> getAll(Specification<Product> spec, Pageable pageable, Function<Product, R> mappingFunction) {
        LogUtil.logInfo("Fetching all products with filters");
        Page<Product> page = productRepository.findAll(spec, pageable);
        List<R> content = page.map(mappingFunction).getContent();
        LogUtil.logInfo("Fetched product: " + content.size());
        return new PageResponse<>(content, new PageResponse.Metadata(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()));
    }

    @Override
    public Product save(Product entity) {
        return productRepository.save(entity);
    }

    @SneakyThrows
    @Transactional
    @Override
    public <M extends GenericsMapper> Product save(Saveable<Product, M> dto, M mapper) {
        LogUtil.logInfo("Saving product: " + dto);
        Product entity = (dto.getId() != null) ? getById(dto.getId()) : new Product();

        if (dto instanceof ProductProperties p) {
            entity.setDiscounts(new HashSet<>(discountService.getAllByDiscountCommonId(p.getDiscountId())));
            entity.setTastes(new HashSet<>(tasteService.getAllByCommonId(p.getTasteId())));
        }

        if (dto instanceof ProductImages pi) {
            if (dto instanceof Uploadable u) {
                updateProductImages(pi, entity, u);
            }
            savePhysicalImages(pi, entity);
        }
        dto.updateEntity(entity, mapper);
        return productRepository.save(entity);
    }

    private void updateProductImages(ProductImages dto, Product entity, Uploadable u) {
        processImage(dto.getImage1(), dto.getPathToImage1(), entity::setPathToImage1, entity, "pathToImage1", u);
        processImage(dto.getImage2(), dto.getPathToImage2(), entity::setPathToImage2, entity, "pathToImage2", u);
        processImage(dto.getImage3(), dto.getPathToImage3(), entity::setPathToImage3, entity, "pathToImage3", u);
        processImage(dto.getImage4(), dto.getPathToImage4(), entity::setPathToImage4, entity, "pathToImage4", u);

        processImage(dto.getImageDescription(), dto.getPathToImageDescription(), entity::setPathToImageDescription, entity, "pathToImageDescription", u);
        processImage(dto.getImagePacking(), dto.getPathToImagePacking(), entity::setPathToImagePacking, entity, "pathToImagePacking", u);
        processImage(dto.getImagePayment(), dto.getPathToImagePayment(), entity::setPathToImagePayment, entity, "pathToImagePayment", u);
        processImage(dto.getImageDelivery(), dto.getPathToImageDelivery(), entity::setPathToImageDelivery, entity, "pathToImageDelivery", u);
    }

    private void savePhysicalImages(ProductImages dto, Product entity) {
        imageServiceImp.save(dto.getImage1(), entity.getPathToImage1());
        imageServiceImp.save(dto.getImage2(), entity.getPathToImage2());
        imageServiceImp.save(dto.getImage3(), entity.getPathToImage3());
        imageServiceImp.save(dto.getImage4(), entity.getPathToImage4());

        imageServiceImp.save(dto.getImageDescription(), entity.getPathToImageDescription());
        imageServiceImp.save(dto.getImagePacking(), entity.getPathToImagePacking());
        imageServiceImp.save(dto.getImagePayment(), entity.getPathToImagePayment());
        imageServiceImp.save(dto.getImageDelivery(), entity.getPathToImageDelivery());
    }

    @SneakyThrows
    private void processImage(MultipartFile image, String imagePath, Consumer<String> pathSetter, Product entity, String fieldName, Uploadable u) {
        if (entity != null && imagePath != null && imagePath.isEmpty()) {
            ImageUtil.deleteImageIfEmpty(entity, fieldName, imageServiceImp);
        }
        if (image != null && !image.isEmpty()) {
            pathSetter.accept(imageServiceImp.generatePath(image, u));
        } else {
            pathSetter.accept(imagePath);
        }
    }

    @Override
    public Product getById(Long id) {
        LogUtil.logInfo("Fetching product by ID: " + id);
        return productRepository.findById(id).orElseThrow(() -> {
            LogUtil.logError("Product not found with ID: " + id, null);
            return new EntityNotFoundException("Product with id: " + id + " was not found!");
        });
    }

    @Override
    public Product getByArticleId(Long id) {
        LogUtil.logInfo("Fetching product by article ID: " + id);
        return productRepository.getByArticleId(id).orElseThrow(() -> {
            LogUtil.logError("Product not found with article ID: " + id, null);
            return new EntityNotFoundException("Product with article id:" + id + " was not found!");
        });
    }

    @Override
    public <R> R getByIdLikeDTO(Long id, Function<Product,R> mappingFunction) {
        LogUtil.logInfo("Fetching product for DTO by ID: " + id);
        Product product = getById(id);
        R dto = mappingFunction.apply(product);
        if(dto instanceof PricedResponse pr){
            pr.setPrices(historyPricesService
                            .getLatestPriceByProductIdInDtoForProduct(product.getId()));
        }
        return dto;
    }

    @Override
    public void deleteById(Long id) throws IOException {
        LogUtil.logInfo("Deleting product by ID: " + id);
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

        List<OrderDetail> allByProductsContaining = orderDetailService.findAllByProductsContaining(product);
        for (OrderDetail detail : allByProductsContaining) {
            detail.getProducts().remove(product);
            orderDetailService.save(detail);
        }

        productRepository.deleteById(id);
        LogUtil.logInfo("Product deleted successfully: " + id);
    }

    @Override
    public boolean isExistByArticleId(Long id) {
        LogUtil.logInfo("Checking if product exists by article ID: " + id);
        return productRepository.existsByArticleId(id);
    }

    @Override
    public boolean isExistById(Long id) {
        LogUtil.logInfo("Checking if product exists by ID: " + id);
        return productRepository.existsById(id);
    }

    @Override
    public List<ProductResponseForViewInTable> getRandomProductsBySizeForDto(int size, LanguageCode code) {
        return productRepository.findSortedProductsBySize(size).stream().map(p -> {
            p.setHistoryPrices(historyPricesService.getLastTwoDataByProduct(p));
            return productMapper.toResponseForViewInMain(p, code);
        }).toList();
    }

    @Override
    public List<Product> getRandomProductsBySize(int size) {
        return productRepository.findSortedProductsBySize(size).stream()
                .peek(p -> p.setHistoryPrices(
                        historyPricesService.getLastTwoDataByProduct(p))).toList();
    }

    @Override
    public List<Integer> getAllMasses() {
        return productRepository.getAllMasses();
    }

    @Override
    public void decreaseCountItems(Long productId) {
        LogUtil.logInfo("Decreasing count of product ID: " + productId);
        Product product = getById(productId);
        if (product.getTotalCount() > 0) {
            product.setTotalCount(product.getTotalCount() - 1);
        }
    }

    @Override
    public void increaseCountItems(Long productId) {
        LogUtil.logInfo("Increasing count of product ID: " + productId);
        Product product = getById(productId);
        product.setTotalCount(product.getTotalCount() + 1);
    }
}