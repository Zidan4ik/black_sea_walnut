package org.example.black_sea_walnut.config;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.*;
import org.example.black_sea_walnut.entity.translation.*;
import org.example.black_sea_walnut.enums.*;
import org.example.black_sea_walnut.repository.*;
import org.example.black_sea_walnut.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {
    private final NewService newService;
    private final TransactionsService transactionsService;
    private final ProductService productService;
    private final HistoryService historyService;
    private final ContactService contactService;
    private final OrderRepository orderRepository;
    private final CountryService countryService;
    private final RegionService regionService;
    private final CityService cityService;
    private final ClientCategoryService clientCategoryService;
    private final DiscountService discountService;
    private final TasteService tasteService;
    private final GalleryRepository galleryRepository;
    private final NutRepository nutRepository;
    private final CallRepository callRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final DeliverPriceRepository deliverPriceRepository;
    private final PasswordEncoder passwordEncoder;
    private final ManagerRepository managerRepository;

    private final Random random = new Random();
    private Faker faker = new Faker();

    @Override
    public void run(String... args) throws Exception {

        if (newService.getAll().isEmpty()) {
            for (int i = 0; i < 3; i++) {
                New new_ = new New();
                new_.setActive(faker.bool().bool());
                new_.setDateOfPublication(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                new_.setPathToMedia("/image/default-image-nut.jpg");
                new_.setMediaType(MediaType.image);

                List<NewTranslation> translations = Arrays.stream(LanguageCode.values())
                        .map(lang -> {
                            faker = new Faker(new Locale(String.valueOf(lang)));
                            NewTranslation t = new NewTranslation();
                            t.setLanguageCode(lang);
                            t.setTitle(faker.commerce().department());
                            t.setDescription(faker.lorem().paragraph());
                            t.setNew_(new_);
                            return t;
                        }).toList();
                new_.setTranslations(translations);
                newService.save(new_);
            }
        }

        if (countryService.getAll().isEmpty()) {
            IntStream.range(0,3).forEach(i->{
                Country country = countryService.save(new Country(faker.country().name()));
                IntStream.range(0,3).forEach(r->{
                    Region region = regionService.save(new Region(faker.address().state(),country));
                    IntStream.range(0,3).forEach(c->{
                        cityService.save(new City(faker.address().city(), region));
                    });
                });
            });
        }

        if (historyService.getAll().isEmpty()) {
            History historyMain = new History(null, true, PageType.main_banner, null, new ArrayList<>(), new ArrayList<>());
            historyMain.setBanner(new Banner(null, "/image/default-image-nut.jpg", MediaType.image, historyMain));
            historyMain.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Головний банер", "Підзаголовок", "Опис головного банера", historyMain));
            historyMain.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Main Banner", "Subtitle", "Main banner description", historyMain));
            historyMain.getHistoryMedia().add(new HistoryMedia(null, MediaType.image, "/image/default-image-nut.jpg", historyMain));
            historyMain.getHistoryMedia().add(new HistoryMedia(null, MediaType.image, "/image/default-image-nut.jpg", historyMain));
            historyService.save(historyMain);

            History historyProduction = new History(null, true, PageType.main_production, null, new ArrayList<>(), new ArrayList<>());
            historyProduction.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Виробництво", "Підзаголовок", "Опис виробництва", historyProduction));
            historyProduction.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Production", "Subtitle", "Production description", historyProduction));
            historyService.save(historyProduction);

            History historyFactory = new History(null, true, PageType.main_factory_about, null, new ArrayList<>(), new ArrayList<>());

            historyFactory.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Про фабрику", "Підзаголовок", "Опис про фабрику", historyFactory));
            historyFactory.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "About Factory", "Subtitle", "About factory description", historyFactory));

            historyFactory.getHistoryMedia().add(new HistoryMedia(null, MediaType.image, "/image/default-image-nut.jpg", historyFactory));
            historyFactory.getHistoryMedia().add(new HistoryMedia(null, MediaType.image, "/image/default-image-nut.jpg", historyFactory));

            historyService.save(historyFactory);


            History historyNumber = new History(null, true, PageType.main_numbers, null, new ArrayList<>(), new ArrayList<>());
            historyNumber.getTranslations().add(new HistoryTranslation(
                    LanguageCode.uk,
                    "240", "1", "10", "80",
                    "Змішаних садів", "Тепличного комплексу", "Перша промислова плантація шипшини в Україні",
                    "Приживання саджанців",
                    historyNumber
            ));
            historyNumber.getTranslations().add(new HistoryTranslation(
                    LanguageCode.en,
                    "Number 1", "Number 2", "Number 3", "Number 4",
                    "Mixed gardens", "Greenhouse complex",
                    "The first industrial rosehip plantation in Ukraine", "Survival of seedlings",
                    historyNumber
            ));
            historyService.save(historyNumber);

            History historyAim = new History(null, true, PageType.main_aim, null, new ArrayList<>(), new ArrayList<>());
            historyAim.setBanner(new Banner(null, "/image/default-image-nut.jpg", MediaType.image, historyAim));
            historyAim.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Мета", "Підзаголовок", "Опис мети", historyAim));
            historyAim.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Aim", "Subtitle", "Aim description", historyAim));
            historyService.save(historyAim);

            History historyEcoProduction = new History(null, true, PageType.main_eco_production, null, new ArrayList<>(), new ArrayList<>());
            historyEcoProduction.setBanner(new Banner(null, "/image/default-image-nut.jpg", MediaType.image, historyEcoProduction));
            historyEcoProduction.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Еко-виробництво", "Підзаголовок", "Опис еко-виробництва", historyEcoProduction));
            historyEcoProduction.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Eco Production", "Subtitle", "Eco production description", historyEcoProduction));
            historyService.save(historyEcoProduction);

            History catalogBanner = new History(null, true, PageType.catalog_banner, null, new ArrayList<>(), new ArrayList<>());
            catalogBanner.setBanner(new Banner(null, "/image/default-image-nut.jpg", MediaType.image, catalogBanner));
            catalogBanner.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Каталог банер", "Підзаголовок", "Опис банера каталогу", catalogBanner));
            catalogBanner.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Catalog Banner", "Subtitle", "Catalog banner description", catalogBanner));
            catalogBanner.getHistoryMedia().add(new HistoryMedia(null, MediaType.image, "/image/default-image-nut.jpg", historyFactory));
            catalogBanner.getHistoryMedia().add(new HistoryMedia(null, MediaType.image, "/image/default-image-nut.jpg", historyFactory));


            historyService.save(catalogBanner);

            History catalogEcologically = new History(null, true, PageType.catalog_ecologically_pure_walnut, null, new ArrayList<>(), new ArrayList<>());
            catalogEcologically.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Екологічно чистий горіх", "Підзаголовок", "Опис", catalogEcologically));
            catalogEcologically.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Ecologically Pure Walnut", "Subtitle", "Description", catalogEcologically));
            historyService.save(catalogEcologically);

            History factoryBanner = new History(null, true, PageType.factory_banner, null, new ArrayList<>(), new ArrayList<>());
            factoryBanner.setBanner(new Banner(null, "/image/default-image-nut.jpg", MediaType.image, factoryBanner));
            factoryBanner.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Банер фабрики", "Підзаголовок", "Опис банера фабрики", factoryBanner));
            factoryBanner.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Factory Banner", "Subtitle", "Factory banner description", factoryBanner));
            historyService.save(factoryBanner);

            History catalogBlock = new History(null, true, PageType.factory_block2, null, new ArrayList<>(), new ArrayList<>());
            catalogBlock.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Блок фабрики", "Підзаголовок", "Опис блоку", catalogBlock));
            catalogBlock.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Factory Block", "Subtitle", "Block description", catalogBlock));
            historyService.save(catalogBlock);

            History clientBanner = new History(null, true, PageType.clients_banner, null, new ArrayList<>(), new ArrayList<>());
            clientBanner.setBanner(new Banner(null, "/image/default-image-nut.jpg", MediaType.image, clientBanner));
            clientBanner.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Банер клієнтів", "Підзаголовок", "Опис банера клієнтів", clientBanner));
            clientBanner.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Clients Banner", "Subtitle", "Clients banner description", clientBanner));
            historyService.save(clientBanner);

            History clientEcoProduction = new History(null, true, PageType.clients_eco_production, null, new ArrayList<>(), new ArrayList<>());
            clientEcoProduction.setBanner(new Banner(null, "/image/default-image-nut.jpg", MediaType.image, clientEcoProduction));
            clientEcoProduction.getTranslations().add(new HistoryTranslation(null, LanguageCode.uk, "Еко-виробництво для клієнтів", "Підзаголовок", "Опис еко-виробництва", clientEcoProduction));
            clientEcoProduction.getTranslations().add(new HistoryTranslation(null, LanguageCode.en, "Eco Production for Clients", "Subtitle", "Eco production description", clientEcoProduction));
            historyService.save(clientEcoProduction);
        }
        if (clientCategoryService.getAll().isEmpty()) {
            ClientCategory clientCategory1 = new ClientCategory();
            clientCategory1.setActive(true);
            clientCategory1.setPathToImage("/image/default-image-nut.jpg");
            clientCategory1.setPathToSvg("/image/default-image-nut.jpg");
            clientCategory1.setMediaTypeImage(MediaType.image);
            clientCategory1.setMediaTypeSvg(MediaType.image);

            clientCategory1.getTranslations().add(new ClientCategoryTranslation(
                    null,
                    LanguageCode.uk,
                    "Категорія Клієнтів 1",
                    "Підзаголовок 1",
                    "Опис першої категорії клієнтів",
                    clientCategory1
            ));

            clientCategory1.getTranslations().add(new ClientCategoryTranslation(
                    null,
                    LanguageCode.en,
                    "Client Category 1",
                    "Subtitle 1",
                    "Description for the first client category",
                    clientCategory1
            ));

            ClientCategory clientCategory2 = new ClientCategory();
            clientCategory2.setActive(true);
            clientCategory2.setPathToImage("/image/default-image-nut.jpg");
            clientCategory2.setPathToSvg("/image/default-image-nut.jpg");
            clientCategory2.setMediaTypeImage(MediaType.image);
            clientCategory2.setMediaTypeSvg(MediaType.image);

            clientCategory2.getTranslations().add(new ClientCategoryTranslation(
                    null,
                    LanguageCode.uk,
                    "Категорія Клієнтів 2",
                    "Підзаголовок 2",
                    "Опис другої категорії клієнтів",
                    clientCategory2
            ));

            clientCategory2.getTranslations().add(new ClientCategoryTranslation(
                    null,
                    LanguageCode.en,
                    "Client Category 2",
                    "Subtitle 2",
                    "Description for the second client category",
                    clientCategory2
            ));

            clientCategoryService.save(clientCategory1);
            clientCategoryService.save(clientCategory2);
        }
        if (transactionsService.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                Transaction transaction = new Transaction();
                transaction.setCustomer(faker.name().fullName());
                transaction.setDate(LocalDateTime.now().minusDays(i));
                transaction.setSumma(1000 * i);
                transaction.setPhone(faker.numerify("+380#########"));
                transaction.setEmail(faker.internet().emailAddress());
                transaction.setPaymentType(faker.options().option(PaymentType.class));
                transaction.setPaymentStatus(faker.options().option(PaymentStatus.class));
                transaction.setUser(null);
                transactionsService.save(transaction);
            }
        }

        if (discountService.getAll().isEmpty()) {
            List<Discount> discounts = new ArrayList<>();
            discounts.add(new Discount(null,(long)1,LanguageCode.uk,"Новий",15));
            discounts.add(new Discount(null,(long)1,LanguageCode.en,"New",15));

            discounts.add(new Discount(null,(long)2,LanguageCode.uk,"Cезон",50));
            discounts.add(new Discount(null,(long)2,LanguageCode.en,"Seasonal",50));

            discountService.saveAll(discounts);
        }
        if (tasteService.getAll().isEmpty()) {
            List<Taste> tastes = new ArrayList<>();

            tastes.add(new Taste(null, (long) 1, LanguageCode.uk,"Солодкий"));
            tastes.add(new Taste(null, (long) 1, LanguageCode.en,"Sweet"));
            tastes.add(new Taste(null, (long) 2, LanguageCode.uk,"Класичний"));
            tastes.add(new Taste(null, (long) 2, LanguageCode.en,"Classic"));

            tasteService.saveAll(tastes);
        }

        if (productService.getAll().isEmpty()) {
            Map<Long, List<Discount>> discountGrouped = discountService.findAllGroupedByCommonId();
            List<Long> keysDiscounts = new ArrayList<>(discountGrouped.keySet());

            Map<Long, List<Taste>> tastesGrouped = tasteService.getAllGroupedByCommonId();
            ArrayList<Long> keysTastes = new ArrayList<>(tastesGrouped.keySet());

            for (int i = 1; i <= 10; i++) {
                Product product = new Product();
                product.setArticleId((long) i);
                product.setActive(faker.random().nextBoolean());
                product.setTotalCount((long) faker.number().numberBetween(1, 100));
                product.setCreatedDate(LocalDateTime.now().minusDays(i));
                product.setPathToImage1("/image/default-image-nut.jpg");
                product.setPathToImage2("/image/default-image-nut.jpg");
                product.setPathToImage3("/image/default-image-nut.jpg");
                product.setPathToImage4("/image/default-image-nut.jpg");
                product.setMass(faker.number().numberBetween(1, 1000));
                product.setMassEnergy(faker.number().numberBetween(1, 1000));

                product.setPathToImageDescription("/image/default-image-nut.jpg");
                product.setPathToImagePayment("/image/default-image-nut.jpg");
                product.setPathToImagePacking("/image/default-image-nut.jpg");
                product.setPathToImageDelivery("/image/default-image-nut.jpg");

                product.getProductTranslations().add(new ProductTranslation(
                        LanguageCode.uk,
                        "Продукт " + i,
                        faker.lorem().paragraph(),
                        faker.lorem().paragraph(),
                        faker.lorem().paragraph(),
                        faker.lorem().paragraph(),
                        faker.lorem().paragraph(),
                        faker.lorem().paragraph(),
                        product
                ));
                product.getProductTranslations().add(new ProductTranslation(
                        LanguageCode.en,
                        "Product " + i,
                        faker.lorem().paragraph(),
                        faker.lorem().paragraph(),
                        faker.lorem().paragraph(),
                        faker.lorem().paragraph(),
                        faker.lorem().paragraph(),
                        faker.lorem().paragraph(),
                        product
                ));

                String priceByUnit = String.valueOf(faker.number().numberBetween(100, 1000));
                product.setPriceByUnit(priceByUnit);
                product.setHistoryPrices(new ArrayList<>());
                product.getHistoryPrices().add(new HistoryPrices(
                        Integer.parseInt(priceByUnit),
                        LocalDateTime.now().minusDays(i),
                        LocalDateTime.now().plusDays(i),
                        product
                ));
                Long randomCommonIdDiscount = keysDiscounts.get(faker.random().nextInt(keysDiscounts.size()));
                product.setDiscounts(new HashSet<>(discountGrouped.get(randomCommonIdDiscount)));

                Long randomCommonIdTastes = keysTastes.get(faker.random().nextInt(keysTastes.size()));
                product.setTastes(new HashSet<>(tastesGrouped.get(randomCommonIdTastes)));
                productService.save(product);
            }
        }
        if (galleryRepository.findAll().isEmpty()) {
            List<Gallery> galleries = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Gallery gallery = new Gallery();
                gallery.setMediaType(faker.options().option(MediaType.class));
                gallery.setActive(random.nextBoolean());
                gallery.setTimeUpdate(LocalDateTime.now().minusDays(random.nextInt(30)));
                gallery.setPathToMedia("/image/default-image-nut.jpg");
                List<GalleryTranslation> translations = new ArrayList<>();
                for (LanguageCode language : LanguageCode.values()) {
                    GalleryTranslation translation = new GalleryTranslation();
                    translation.setLanguageCode(language);
                    translation.setTitle(faker.book().title());
                    translation.setDescription(faker.lorem().paragraph());
                    translation.setGallery(gallery);
                    translations.add(translation);
                }
                gallery.setTranslations(translations);
                galleries.add(gallery);
            }
            galleryRepository.saveAll(galleries);
        }
        if (nutRepository.findAll().isEmpty()) {
            List<Nut> nuts = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                Nut nut = new Nut();
                nut.setActive(random.nextBoolean());
                nut.setPathToImage("/image/default-image-nut.jpg");
                nut.setPathToSvg("/image/default-image-nut.jpg");
                nut.setDate(LocalDate.now().minusDays(random.nextInt(365)));

                List<NutTranslation> translations = new ArrayList<>();
                for (LanguageCode language : LanguageCode.values()) {
                    NutTranslation translation = new NutTranslation();
                    translation.setLanguageCode(language);
                    translation.setTitle(faker.food().fruit());
                    translation.setDescription(faker.lorem().paragraph());
                    translation.setNut(nut);
                    translations.add(translation);
                }
                nut.setTranslations(translations);
                nuts.add(nut);
                nutRepository.save(nut);
            }
        }
        if (callRepository.findAll().isEmpty()) {
            List<Call> calls = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Call call = new Call();
                call.setPhone(faker.numerify("+380#########"));
                call.setStatus(faker.options().option(CallStatus.class));
                call.setRegisterCall(LocalDateTime.now().minusDays(random.nextInt(30)));
                calls.add(call);
            }
            callRepository.saveAll(calls);
        }
        if (contactRepository.findAll().isEmpty()) {
            Contact contact = Contact.builder()
                    .phone1(faker.numerify("+380#########"))
                    .phone2(faker.numerify("+380#########"))
                    .email("walnut@gmail.com")
                    .addressWork("4145 Leonor Locks")
                    .addressFactory("9051 Mertz Corners")
                    .coordinates("51.5,-0.09")
                    .telegram("https://t.me/AlFr1eD")
                    .viber("https://invite.viber.com/?g=j9MxXBxkQVTtvxKDAEHOYrzC1lfBMp1K")
                    .whatsapp("https://chat.whatsapp.com/Iv5YyuF7MI3KQe0N60jsry")
                    .facebook("https://www.facebook.com/WalnutAI/")
                    .instagram("https://www.instagram.com/walnut.wanders/")
                    .youtube("https://www.youtube.com/channel/UCk0ZtztoY8TwR4jb83vnyvw")
                    .timeUpdated(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)))
                    .build();
            contactRepository.save(contact);
        }
        if (userRepository.findAll().isEmpty()) {
            User admin = new User();
            admin.setFullName("Admin User");
            admin.setEmail("admin@gmail.com");
            admin.setPhone(faker.numerify("+380#########"));
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setPaymentDetails("Bank Account: 1234-5678");
            admin.setDateRegistered(LocalDate.now());
            admin.setRegisterType(RegisterType.individual);
            admin.setFop(true);
            admin.setEnable(true);
            admin.setStatus(UserStatus.isActive);
            admin.setRole(Role.ADMIN);
            admin.setDepartment(1);
            admin.setAddress("Admin Address");
            admin.setCompany("Admin Company");

            User user = new User();
            user.setFullName("User");
            user.setEmail("user@gmail.com");
            user.setPhone(faker.numerify("+380#########"));
            user.setPassword(passwordEncoder.encode("user"));
            user.setPaymentDetails("Bank Account: 8765-4321");
            user.setDateRegistered(LocalDate.now());
            user.setRegisterType(RegisterType.legal);
            user.setFop(false);
            user.setEnable(true);
            user.setStatus(UserStatus.isActive);
            user.setRole(Role.USER);
            user.setDepartment(2);
            user.setAddress("User Address");
            user.setCompany("User Company");

            userRepository.save(admin);
            userRepository.save(user);
        }
        if (orderRepository.findAll().isEmpty()) {
            List<Order> orders = new ArrayList<>();
            LocalDate today = LocalDate.now();
            int year = today.getYear();
            int month = today.getMonthValue();
            int daysInMonth = today.lengthOfMonth();
            for (int i = 0; i < 50; i++) {
                Order order = new Order();
                order.setPersonalId(faker.number().randomNumber());
                order.setFio(faker.name().fullName());
                order.setEmail(faker.internet().emailAddress());
                order.setPhone(faker.numerify("+380#########"));
                order.setCountProducts(faker.number().numberBetween(1, 10));
                order.setTotalPrice(faker.number().numberBetween(100, 1000));
                order.setCompanyDelivery(faker.company().name());
                order.setPersonNameDelivery(faker.name().fullName());
                order.setEmailDelivery(faker.internet().emailAddress());
                order.setPhoneDelivery(faker.numerify("+380#########"));
                order.setAddressDelivery(faker.address().fullAddress());
                order.setOrderStatus(faker.options().option(OrderStatus.class));
                order.setDeliveryStatus(faker.options().option(DeliveryStatus.class));
                order.setDeliveryType(faker.options().option(DeliveryType.class));
                order.setPaymentType(faker.options().option(PaymentType.class));
                order.setPayed(faker.bool().bool());

                int randomDay = new Random().nextInt(daysInMonth) + 1;
                LocalDate randomDate = LocalDate.of(year, month, randomDay);
                order.setDateOfOrdering(randomDate);

                List<OrderDetail> orderDetails = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setProductName(faker.commerce().productName());
                    orderDetail.setUnitPrice(faker.number().numberBetween(50, 500));
                    orderDetail.setCount(faker.number().numberBetween(1, 5));
                    orderDetail.setOrder(order);
                    orderDetails.add(orderDetail);
                }
                order.setOrderDetails(orderDetails);

                List<DeliveryPrice> deliveryPrices = new ArrayList<>();
                for (int k = 0; k < 2; k++) {
                    DeliveryPrice deliveryPrice = new DeliveryPrice();
                    deliveryPrice.setPrice(faker.number().numberBetween(10, 100));
                    deliverPriceRepository.save(deliveryPrice);
                    deliveryPrices.add(deliveryPrice);
                }
                order.setDeliveryPrices(deliveryPrices);

                order.setUser(userRepository.findAll().get(faker.number().numberBetween(0, (int) userRepository.count())));
                orders.add(order);
            }
            orderRepository.saveAll(orders);
        }
        if (managerRepository.findAll().isEmpty()) {
            List<Manager> managers = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Manager manager = new Manager();
                manager.setPhone(faker.numerify("+380#########"));
                List<ManagerTranslation> translations = new ArrayList<>();
                for (LanguageCode languageCode : LanguageCode.values()) {
                    ManagerTranslation translation = new ManagerTranslation(
                            languageCode,
                            faker.name().firstName(),
                            faker.name().lastName(),
                            manager
                    );
                    translations.add(translation);
                }

                manager.setTranslations(translations);
                managers.add(manager);
            }
            managerRepository.saveAll(managers);
        }
    }

    private static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        T[] values = clazz.getEnumConstants();
        return values[new Random().nextInt(values.length)];
    }
}
