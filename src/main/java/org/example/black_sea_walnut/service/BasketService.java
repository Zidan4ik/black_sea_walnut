package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.entity.Basket;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;

public interface BasketService {
    List<BasketResponseForCart> getAllInResponseForCart(User user, LanguageCode code);

    List<Basket> getAll();

    Basket getById(Long id);

    void decreaseAmountById(Long id);

    void increaseAmountById(Long id);

    void deleteById(Long id);

    void saveCountProduct(Long basketId, Integer value);

    void save(Basket basket);

    void buyProduct(Long idProduct, User currentUser);

    void deleteByUser(User user);
}
