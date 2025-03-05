package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.web.BasketResponseForCart;
import org.example.black_sea_walnut.entity.Basket;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.mapper.BasketMapper;
import org.example.black_sea_walnut.repository.BasketRepository;
import org.example.black_sea_walnut.service.BasketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketServiceImp implements BasketService {
    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;

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
        basket.setCount(basket.getCount() + 1);
    }

    @Override
    public void deleteById(Long id) {
        basketRepository.deleteById(id);
    }

    @Override
    public void save(Basket basket) {
        basketRepository.save(basket);
    }
}
