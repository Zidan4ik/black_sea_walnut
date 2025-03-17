package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.entity.Basket;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.exception.InsufficientStockException;
import org.example.black_sea_walnut.mapper.BasketMapper;
import org.example.black_sea_walnut.mapper.ProductMapper;
import org.example.black_sea_walnut.repository.BasketRepository;
import org.example.black_sea_walnut.service.BasketService;
import org.example.black_sea_walnut.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketServiceImp implements BasketService {
    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @Override
    public List<BasketResponseForCart> getAllInResponseForCart(User user) {
        return basketRepository.getAllByUser(user).stream().map(basketMapper::toResponseForCart).toList();
    }

    @Override
    public List<Basket> getAll() {
        return basketRepository.findAll();
    }

    @Override
    public Basket getById(Long id) {
        return basketRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product with id: " + id + " was not found!"));
    }

    @Override
    @Transactional
    public void decreaseAmountById(Long id) {
        Basket basket = getById(id);
        Product product = basket.getProducts().get(0);
        productService.increaseCountItems(product.getId());
        if (basket.getCount() <= 1) {
            deleteById(id);
        } else {
            basket.setCount(basket.getCount() - 1);
        }
    }

    @Override
    @Transactional
    public void increaseAmountById(Long id) {
        Basket basket = getById(id);
        Product product = basket.getProducts().get(0);
        if (product.getTotalCount() - 1 < 0) {
            throw new InsufficientStockException("Товарів більше немає");
        }
        productService.decreaseCountItems(product.getId());
        basket.setCount(basket.getCount() + 1);
    }

    @Override
    public void deleteById(Long id) {
        basketRepository.deleteById(id);
    }

    @Override
    public void saveCountProduct(Long  basketId, Integer value) {
        Basket basketById = getById(basketId);
        Product product = basketById.getProducts().get(0);
        int countDifference = value - basketById.getCount();
        if (product.getTotalCount() - countDifference < 0) {
            throw new InsufficientStockException("На складі лишилось лише: " + product.getTotalCount() + " товару!");
        }
        Product productFromBD = productService.getById(product.getId());
        productFromBD.setTotalCount(product.getTotalCount() - countDifference);
        basketById.setCount(value);
        save(basketById);
    }

    @Override
    public void save(Basket basket) {
        basketRepository.save(basket);
    }

    @Override
    @Transactional
    public void buyProduct(Long idProduct, User currentUser) {
        if (idProduct == null) return;

        Product product = productService.getById(idProduct);


        if (product.getTotalCount() - 1 < 0) {
            throw new InsufficientStockException("Товарів більше немає");
        }

        String productName = product.getProductTranslations().stream()
                .filter(t -> t.getLanguageCode().equals(LanguageCode.uk))
                .map(ProductTranslation::getName)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found"));

        Basket basket = basketRepository.getBasketByUserAndProductName(currentUser, productName);
        if (basket == null) {
            basket = new Basket();
            basket.setProducts(List.of(product));
            basket.setUser(currentUser);
            basket.setProductName(productName);
            basket.setCount(1);
            basket.setMass(product.getMass());
        } else {
            basket.setCount(basket.getCount() + 1);
        }

        applyDiscounts(basket, product);
        productService.decreaseCountItems(idProduct);
        save(basket);
    }

    private void applyDiscounts(Basket basket, Product product) {
        int unitPrice = Integer.parseInt(product.getPriceByUnit());
        int discountPercent = product.getDiscounts().isEmpty() ? 1 : product.getDiscounts().iterator().next().getValue();
        int discountSumForUnit = (unitPrice * discountPercent) / 100;
        int discountUnitPrice = unitPrice - discountSumForUnit;
        int summaWithoutDiscount = unitPrice * basket.getCount();
        int summaDiscount = discountSumForUnit * basket.getCount();
        int summaWithDiscount = summaWithoutDiscount - summaDiscount;

        basket.setUnitPrice(unitPrice);
        basket.setDiscountPercent(discountPercent);
        basket.setDiscountSumForUnit(discountSumForUnit);
        basket.setDiscountUnitPrice(discountUnitPrice);
        basket.setSummaWithoutDiscount(summaWithoutDiscount);
        basket.setSummaDiscount(summaDiscount);
        basket.setSummaWithDiscount(summaWithDiscount);
    }
}
