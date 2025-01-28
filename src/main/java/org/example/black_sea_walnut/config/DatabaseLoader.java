package org.example.black_sea_walnut.config;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.*;
import org.example.black_sea_walnut.entity.translation.ProductTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.repository.ProductRepository;
import org.example.black_sea_walnut.repository.TransactionsRepository;
import org.example.black_sea_walnut.service.HistoryMainService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.NewService;
import org.example.black_sea_walnut.util.FakerUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;


import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {
    private final NewService newService;
    private final TransactionsRepository transactionsRepository;
    private final ProductRepository productRepository;
    private final HistoryService historyService;

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
        } else if (productRepository.findAll().isEmpty()) {
            for (int i = 0; i < 3; i++) {
                Product product = FakerUtil.fill(Product.class);

                Discount discount = FakerUtil.fill(Discount.class);
                product.setDiscounts(Set.of(discount));

                Taste taste = FakerUtil.fill(Taste.class);
                product.setTastes(Set.of(taste));

                ProductTranslation eng = FakerUtil.fill(ProductTranslation.class);
                eng.setLanguageCode(LanguageCode.en);
                ProductTranslation ukr = FakerUtil.fill(ProductTranslation.class);
                ukr.setLanguageCode(LanguageCode.uk);
                product.setProductTranslations(List.of(ukr, eng));

                productRepository.save(product);
            }
        }
        if (historyService.getAll().isEmpty()) {
            History historyMain = new History(null, false, PageType.main_banner, null, null, null);
            historyMain.setBanner(new Banner(null, null, null, historyMain));
            History historyProduction = new History(null, false, PageType.main_production, null, null, null);
            History historyFactory = new History(null, false, PageType.main_factory_about, null, null, null);
            History historyNumber = new History(null, false, PageType.main_numbers, null, null, null);
            History historyAim = new History(null, false, PageType.main_aim, null, null, null);
            historyAim.setBanner(new Banner(null, null, null, historyAim));
            History historyEcoProduction = new History(null, false, PageType.main_eco_production, null, null, null);
            historyEcoProduction.setBanner(new Banner(null, null, null, historyEcoProduction));

            historyService.save(historyMain);
            historyService.save(historyProduction);
            historyService.save(historyFactory);
            historyService.save(historyNumber);
            historyService.save(historyAim);
            historyService.save(historyEcoProduction);

            History catalogBanner = new History(null, false, PageType.catalog_banner, null, null, null);
            catalogBanner.setBanner(new Banner(null, null, null, catalogBanner));
            History catalogEcologically = new History(null, false, PageType.catalog_ecologically_pure_walnut, null, null, null);
            historyService.save(catalogBanner);
            historyService.save(catalogEcologically);

            History factoryBanner = new History(null, false, PageType.factory_banner, null, null, null);
            factoryBanner.setBanner(new Banner(null, null, null, factoryBanner));
            History catalogBlock = new History(null, false, PageType.factory_block2, null, null, null);
            historyService.save(factoryBanner);
            historyService.save(catalogBlock);

            History clientBanner = new History(null, false, PageType.clients_banner, null, null, null);
            clientBanner.setBanner(new Banner(null, null, null, clientBanner));
            History clientEcoProduction = new History(null, false, PageType.clients_eco_production, null, null, null);
            clientEcoProduction.setBanner(new Banner(null, null, null, clientEcoProduction));
            historyService.save(clientBanner);
            historyService.save(clientEcoProduction);
        }
    }
}
