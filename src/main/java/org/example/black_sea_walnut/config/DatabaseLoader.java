package org.example.black_sea_walnut.config;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.entity.translation.NewTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.NewService;
import org.example.black_sea_walnut.util.FakerUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {
    private final NewService newService;

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
        }
    }
}
