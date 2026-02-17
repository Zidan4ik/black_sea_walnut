package org.example.black_sea_walnut.service.imp;

import groovy.util.logging.Slf4j;
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
import org.example.black_sea_walnut.repository.BasketRepository;
import org.example.black_sea_walnut.service.BasketService;
import org.example.black_sea_walnut.service.ProductService;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketServiceImp implements BasketService {
    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;
    private final ProductService productService;

    @Override
    public List<BasketResponseForCart> getAllInResponseForCart(User user, LanguageCode code) {
        LogUtil.logInfo("Fetch all products in basket by user's id:" + user.getId());
        List<BasketResponseForCart> products = basketRepository.getAllByUser(user).stream()
                .map(m->basketMapper.toResponseForCart(m,code)).toList();
        LogUtil.logInfo("Fetched " + products.size() + " products from the basket for user's id: " + user.getId());
        return products;
    }

    @Override
    public List<Basket> getAll() {
        LogUtil.logInfo("Fetch all products!");
        List<Basket> products = basketRepository.findAll();
        LogUtil.logInfo("Fetched " + products.size() + " all products!");
        return products;
    }

    @Override
    public Basket getById(Long id) {
        LogUtil.logInfo("Fetched product from basket with ID: " + id);
        Basket basket = basketRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product with id: " + id + " was not found!"));
        LogUtil.logInfo("Fetched product from basket with ID: " + id);
        return basket;
    }

    @Override
    @Transactional
    public void decreaseAmountById(Long id) {
        LogUtil.logInfo("Reduce the amount by 1 for the product with id: " + id);
        Basket basket = getById(id);
        Product product = basket.getProducts().get(0);
        productService.increaseCountItems(product.getId());
        LogUtil.logInfo("Reduced the amount by 1 for product with id: " + id);
        if (basket.getCount() <= 1) {
            deleteById(id);
        } else {
            basket.setCount(basket.getCount() - 1);
            applyDiscounts(basket, product);
        }
    }

    @Override
    @Transactional
    public void increaseAmountById(Long id) {
        LogUtil.logInfo("Increase the amount by 1 for the product with id: " + id);
        Basket basket = getById(id);
        Product product = basket.getProducts().get(0);
        if (product.getTotalCount() - 1 < 0) {
            LogUtil.logError("Insufficient stock for product with id: " + product.getId() + ". Available count: " + product.getTotalCount(),
                    new InsufficientStockException("Товарів більше немає"));
            throw new InsufficientStockException("Товарів більше немає");
        }
        productService.decreaseCountItems(product.getId());
        LogUtil.logInfo("Increased the amount by 1 for the product with id: " + id);
        basket.setCount(basket.getCount() + 1);
        applyDiscounts(basket, product);
    }

    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Attempting to defer product with id: " + id);
        basketRepository.deleteById(id);
        LogUtil.logInfo("Successfully defer product with id: " + id);
    }

    @Override
    public void saveCountProduct(Long basketId, Integer value) {
        LogUtil.logInfo("Saving product count. Product in basket id: " + basketId + ", new value: " + value);
        if (value < 0) {
            LogUtil.logError("Attempted to set a negative count for product in basket with id: " + basketId + ", value: " + value,
                    new IllegalArgumentException("The value must not be negative!"));
            throw new IllegalArgumentException("The value must not be negative!");
        }
        Basket basketById = getById(basketId);
        Product product = basketById.getProducts().get(0);
        int countDifference = value - basketById.getCount();
        if (product.getTotalCount() - countDifference < 0) {
            LogUtil.logError("Insufficient stock for product with id: " + product.getId() + ". Available stock: "
                            + product.getTotalCount() + ", Attempted count change: " + countDifference,
                    new InsufficientStockException("Only: left in stock " + product.getTotalCount() + " products!"));

            throw new InsufficientStockException("Only: left in stock" + product.getTotalCount() + " products!");
        }
        Product productFromBD = productService.getById(product.getId());
        productFromBD.setTotalCount(product.getTotalCount() - countDifference);
        if (value == 0) {
            LogUtil.logInfo("Count is 0, deleting product in basket with id: " + basketId);
            deleteById(basketId);
        } else {
            basketById.setCount(value);
            LogUtil.logInfo("Updated count for product basket with id: " + basketId + " to " + value);
            save(basketById);
            applyDiscounts(basketById, product);
        }
    }

    @Override
    public void save(Basket basket) {
        LogUtil.logInfo("Saving basket...");
        basketRepository.save(basket);
        LogUtil.logInfo("Basket saved successfully.");
    }

    @Override
    @Transactional
    public void buyProduct(Long idProduct, User currentUser) {
        LogUtil.logInfo("Buying product ID: " + idProduct + " for user: " + currentUser.getId());
        if (idProduct == null) return;

        Product product = productService.getById(idProduct);

        if (product.getTotalCount() - 1 < 0) {
            LogUtil.logError("Insufficient stock for product ID: " + idProduct,
                    new InsufficientStockException("No more products!"));
            throw new InsufficientStockException("No more products!");
        }

        Map<LanguageCode, String> names = product.getProductTranslations().stream()
                .collect(Collectors.toMap(
                        ProductTranslation::getLanguageCode,
                        ProductTranslation::getName,
                        (existing, replacement) -> existing
                ));

        Basket basket = basketRepository.getBasketByUserAndArticleId(currentUser,product.getArticleId());
        if (basket == null) {
            basket = new Basket();
            basket.setArticleId(product.getArticleId());
            basket.setProducts(List.of(product));
            basket.setUser(currentUser);
            basket.setProductNameUk(names.get(LanguageCode.uk));
            basket.setProductNameEn(names.get(LanguageCode.en));
            basket.setCount(1);
            basket.setMass(product.getMass());
            LogUtil.logInfo("Created new basket for product id: " + product.getArticleId() + " and user: " + currentUser.getId());
        } else {
            basket.setCount(basket.getCount() + 1);
            LogUtil.logInfo("Updated basket count for product id: " + product.getArticleId() + " and user: " + currentUser.getId());
        }

        productService.decreaseCountItems(idProduct);
        applyDiscounts(basket, product);
        save(basket);
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        LogUtil.logInfo("Deleting all baskets for user ID: " + user.getId());
        basketRepository.deleteByUser(user);
        LogUtil.logInfo("Deleted all baskets for user ID: " + user.getId());
    }

    private void applyDiscounts(Basket basket, Product product) {
        LogUtil.logInfo("Applying discounts for product with id: " + product.getId() + " and basket with id: " + basket.getId());
        int unitPrice = Integer.parseInt(product.getPriceByUnit());
        int discountPercent = product.getDiscounts().isEmpty() ? 0 : product.getDiscounts().iterator().next().getValue();
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
        LogUtil.logInfo("Discounts applied. Basket with id: " + basket.getId() + " has total: " + summaWithDiscount);
    }
}
