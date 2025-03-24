package org.example.black_sea_walnut.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.admin.manager.ManagerResponseForView;
import org.example.black_sea_walnut.entity.Manager;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ManagerSpecification {
    public static Specification<Manager> getSpecification(ManagerResponseForView dto) {
        Specification<Manager> specification = Specification.where(null);
        if (dto.getId() != null) {
            specification = specification.and(hasId(dto.getId()));
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            specification = specification.and(likeName(dto.getName()));
        }
        if (dto.getSurname() != null && !dto.getSurname().isBlank()) {
            specification = specification.and(likeSurname(dto.getSurname()));
        }
        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            specification = specification.and(likePhone(dto.getPhone()));
        }
        return specification;
    }

    private static Specification<Manager> hasId(Long id) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Manager> likePhone(String phone) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("phone"), "%" + phone + "%");
    }
    private static Specification<Manager> likeName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }
    private static Specification<Manager> likeSurname(String surname) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("surname"), "%" + surname + "%");
    }
}