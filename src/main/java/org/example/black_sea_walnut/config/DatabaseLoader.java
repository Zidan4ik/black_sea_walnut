package org.example.black_sea_walnut.config;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.*;
import org.example.black_sea_walnut.entity.translation.*;
import org.example.black_sea_walnut.enums.*;
import org.example.black_sea_walnut.repository.*;
import org.example.black_sea_walnut.service.*;
import org.example.black_sea_walnut.service.contact.ContactService;
import org.example.black_sea_walnut.service.history.HistoryService;
import org.example.black_sea_walnut.service.product.ProductService;
import org.example.black_sea_walnut.service.product.taste.TasteService;
import org.example.black_sea_walnut.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final CountryService countryService;
    private final RegionService regionService;
    private final CityService cityService;
    private final HistoryService historyService;
    private final DiscountService discountService;
    private final TasteService tasteService;
    private final ProductService productService;
    private final ContactRepository contactRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedUsers();
        seedLocations();
        seedCatalogMetadata();
        seedProducts();
        seedContacts();
        seedHistoryBanners();
    }

    private void seedUsers() {
        if (userService.getAll().isEmpty()) {
            User admin = new User();
            admin.setFullName("Головний Адміністратор");
            admin.setEmail("admin@blackseawalnut.com");
            admin.setPhone("+380991112233");
            admin.setPassword("AdminSecure2026!");
            admin.setDateRegistered(LocalDate.now());
            admin.setRegisterType(RegisterType.fop);
            admin.setFop(true);
            admin.setEnable(true);
            admin.setStatus(UserStatus.isActive);
            admin.setRole(Role.SUPER_ADMIN);
            admin.setDepartment(1);
            admin.setAddress("м. Київ, вул. Хрещатик, 1");
            admin.setCompany("Black Sea Walnut LLC");
            admin.setPathToImage("");

            userService.save(admin);
        }
    }

    private void seedLocations() {
        if (countryService.getAll().isEmpty()) {
            Country ukraine = countryService.save(new Country("Україна"));

            Map<String, List<String>> locations = Map.of(
                    "Київська область", List.of("Київ", "Біла Церква", "Бровари"),
                    "Львівська область", List.of("Львів", "Дрогобич", "Стрий")
            );

            locations.forEach((regionName, cities) -> {
                Region region = regionService.save(new Region(regionName, ukraine));
                cities.forEach(cityName -> cityService.save(new City(cityName, region)));
            });
        }
    }

    private void seedCatalogMetadata() {
        if (discountService.getAll().isEmpty()) {
            discountService.saveAll(List.of(
                    new Discount(null, 1L, LanguageCode.uk, "Новинка", 10),
                    new Discount(null, 1L, LanguageCode.en, "New", 10),
                    new Discount(null, 2L, LanguageCode.uk, "Сезонна знижка", 20),
                    new Discount(null, 2L, LanguageCode.en, "Seasonal", 20)
            ));
        }

        if (tasteService.getAll().isEmpty()) {
            tasteService.saveAll(List.of(
                    new Taste(null, 1L, LanguageCode.uk, "Класичний"),
                    new Taste(null, 1L, LanguageCode.en, "Classic"),
                    new Taste(null, 2L, LanguageCode.uk, "Солоний"),
                    new Taste(null, 2L, LanguageCode.en, "Salted")
            ));
        }
    }

    private void seedProducts() {
        if (productService.getAll().isEmpty()) {
            Product walnut = new Product();
            walnut.setArticleId(1001L);
            walnut.setActive(true);
            walnut.setTotalCount(50L);
            walnut.setCreatedDate(LocalDateTime.now());
            walnut.setPathToImage1("/image/default-image-nut.jpg");
            walnut.setMass(500);
            walnut.setPriceByUnit("250");

            walnut.getProductTranslations().add(new ProductTranslation(
                    LanguageCode.uk, "Волоський горіх очищений",
                    "Преміальний очищений волоський горіх високої якості.",
                    "Екологічно чистий продукт.", "Зберігати в сухому місці.",
                    "Доставка по всій Україні.", "Оплата карткою або при отриманні.",
                    "Герметична упаковка.", walnut
            ));

            walnut.getProductTranslations().add(new ProductTranslation(
                    LanguageCode.en, "Peeled Walnut",
                    "Premium quality peeled walnut.",
                    "Ecologically pure product.", "Store in a dry place.",
                    "Worldwide shipping available.", "Card payment or Cash on Delivery.",
                    "Sealed package.", walnut
            ));

            productService.save(walnut);
        }
    }

    private void seedContacts() {
        if (contactRepository.findAll().isEmpty()) {
            Contact contact = Contact.builder()
                    .phone1("+380441112233")
                    .email("info@blackseawalnut.com")
                    .addressWork("м. Київ, вул. Серія, 10")
                    .addressFactory("Одеська обл., м. Южне")
                    .coordinates("46.62,31.10")
                    .telegram("https://t.me/blackseawalnut")
                    .timeUpdated(LocalDateTime.now())
                    .build();
            contactRepository.save(contact);
        }
    }

    private void seedHistoryBanners() {
        if (historyService.getAll().isEmpty()) {
            History historyMain = new History(null, true, PageType.main_banner, null, new ArrayList<>(), new ArrayList<>());
            historyMain.setBanner(new Banner(null, "/image/default-image-nut.jpg", MediaType.image, historyMain));
            historyMain.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Black Sea Walnut", "Екологічні горіхи", "Вирощено з любов'ю в Україні", historyMain));
            historyMain.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Black Sea Walnut", "Organic Nuts", "Grown with love in Ukraine", historyMain));
            historyService.save(historyMain);
        }
    }
}