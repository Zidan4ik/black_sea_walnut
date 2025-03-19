package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.Basket;
import org.example.black_sea_walnut.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    List<Basket> getAllByUser(User user);

    Basket getBasketByUserAndProductName(User user, String productName);

    void deleteByUser(User user);
}
