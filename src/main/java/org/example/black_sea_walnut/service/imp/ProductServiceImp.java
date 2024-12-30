package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.*;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.ProductMapper;
import org.example.black_sea_walnut.repository.ProductRepository;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.service.HistoryPricesService;
import org.example.black_sea_walnut.service.ProductService;
import org.example.black_sea_walnut.service.TasteService;
import org.example.black_sea_walnut.service.specifications.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper = new ProductMapper();
    private final TasteService tasteService;
    private final DiscountService discountService;
    private final HistoryPricesService historyPricesService;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public PageResponse<ResponseProductForView> getAll(ResponseProductForView response, Pageable pageable, LanguageCode code) {
        Page<Product> page = productRepository.findAll(ProductSpecification.getSpecification(response, code), pageable);
        List<ResponseProductForView> responseDTOView = page.map(p -> mapper.toDTOForView(p, code)).stream().toList();
        return new PageResponse<>(responseDTOView, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public Product save(Product entity) {
        return productRepository.save(entity);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product with id: " + id + " was not found!")
        );
    }

    @Override
    public ResponseProductForAdd getByIdLikeDTOAdd(Long id) {
        Product product = getById(id);
        ResponseProductForAdd productInDtoAdd = mapper.toDTOForAdd(product);
        ResponseTastesForProduct tasteInDTOForProduct = tasteService.getByTasteIdInDTO(product.getTaste().getTasteId());
        ResponseDiscountsForProduct discountInDTOForProduct = discountService.getByDiscountIdInDTO(product.getDiscount().getDiscountId());
        ResponseHistoryPricesForProduct historyPricesInDTOForProduct = historyPricesService.getLatestPriceByProductIdInDtoForProduct(product.getId());
        productInDtoAdd.setTaste(tasteInDTOForProduct);
        productInDtoAdd.setDiscount(discountInDTOForProduct);
        productInDtoAdd.setPrices(historyPricesInDTOForProduct);
        return productInDtoAdd;
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
