package org.example.black_sea_walnut.config;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.*;
import org.example.black_sea_walnut.entity.translation.NewTranslation;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.repository.ProductRepository;
import org.example.black_sea_walnut.repository.TransactionsRepository;
import org.example.black_sea_walnut.service.NewService;
import org.example.black_sea_walnut.service.TransactionsService;
import org.example.black_sea_walnut.util.FakerUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {
    private final NewService newService;
    private final TransactionsRepository transactionsRepository;
    private final ProductRepository productRepository;
    @Override
    public void run(String... args) throws Exception {
        if (newService.getAll().isEmpty()) {
            for (int i = 0; i < 3; i++) {
//                New new_ = FakerUtil.fill(New.class);
//                NewTranslation eng = FakerUtil.fill(NewTranslation.class);
//                eng.setLanguageCode(LanguageCode.en);
//                NewTranslation ukr = FakerUtil.fill(NewTranslation.class);
//                ukr.setLanguageCode(LanguageCode.uk);
//                ukr.setNew_(new_);
//                eng.setNew_(new_);
//                new_.setTranslations(List.of(eng, ukr));
//                newService.save(new_);
            }
        } else if (transactionsRepository.findAll().isEmpty()) {
            for (int i = 0; i < 3; i++) {
                Transaction transaction = FakerUtil.fill(Transaction.class);
                transactionsRepository.save(transaction);
            }
        } else if(productRepository.findAll().isEmpty()){
            for (int i = 0; i < 3; i++) {
                Product product = FakerUtil.fill(Product.class);

                Discount discount = FakerUtil.fill(Discount.class);
                product.setDiscount(discount);

                Taste taste = FakerUtil.fill(Taste.class);
                product.setTaste(taste);

                ProductTranslation eng = FakerUtil.fill(ProductTranslation.class);
                eng.setLanguageCode(LanguageCode.en);
                ProductTranslation ukr = FakerUtil.fill(ProductTranslation.class);
                ukr.setLanguageCode(LanguageCode.uk);
                product.setProductTranslations(List.of(ukr, eng));

                productRepository.save(product);
            }
        }
    }
}
