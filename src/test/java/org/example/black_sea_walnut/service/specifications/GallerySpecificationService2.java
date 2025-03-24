package org.example.black_sea_walnut.service.specifications;

import jakarta.persistence.criteria.Join;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.entity.translation.GalleryTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.repository.GalleryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Якщо використовуєте реальну БД
public class GallerySpecificationService2 {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GalleryRepository galleryRepository;

    @Test
    void testFindByLanguageCode_WithJoin() {
        LanguageCode code = LanguageCode.en;

        Gallery gallery = new Gallery();
        entityManager.persist(gallery);

        GalleryTranslation translation = new GalleryTranslation();
        translation.setGallery(gallery);
        translation.setLanguageCode(code);
        entityManager.persist(translation);

        entityManager.flush();

        // Використовуємо ту саму `Specification`
        Specification<Gallery> spec = (root, query, criteriaBuilder) -> {
            Join<Object, Object> translations = root.join("translations");
            return criteriaBuilder.equal(translations.get("languageCode"), code);
        };

        // Act: виконуємо пошук
        List<Gallery> results = galleryRepository.findAll(spec);

        // Assert: перевіряємо результат
        assertEquals(1, results.size());
        assertEquals(gallery.getId(), results.get(0).getId());
    }
}
