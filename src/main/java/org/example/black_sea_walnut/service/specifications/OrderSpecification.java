package org.example.black_sea_walnut.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.ResponseOrderForView;
import org.example.black_sea_walnut.entity.Order;
import org.example.black_sea_walnut.enums.DeliveryStatus;
import org.example.black_sea_walnut.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class OrderSpecification {
    public static Specification<Order> getSpecification(ResponseOrderForView response) {
        Specification<Order> specification = Specification.where(null);
        if (response.getId() != null) {
            specification = specification.and(hasId(response.getId()));
        }
        if (response.getFullName() != null && !response.getFullName().isEmpty()) {
            specification = specification.and(likeFullName(response.getFullName()));
        }
        if (response.getEmail() != null && !response.getEmail().isEmpty()) {
            specification = specification.and(likeEmail(response.getEmail()));
        }
        if (response.getTotalCount() != null) {
            specification = specification.and(hasTotalCount(response.getTotalCount()));
        }
        if (response.getTotalPrice() != null) {
            specification = specification.and(hasTotalPrice(response.getTotalPrice()));
        }
        if (response.getTotalPrice() != null) {
            specification = specification.and(hasTotalPrice(response.getTotalPrice()));
        }
        if (response.getStatusDelivery() != null) {
            specification = specification.and(hasStatusDelivery(response.getStatusDelivery()));
        }
        if (response.getStatusOrder() != null) {
            specification = specification.and(hasStatusOrder(response.getStatusOrder()));
        }
        if (response.getIsPay() != null) {
            specification = specification.and(hasTruePayment(response.getIsPay()));
        }
        if (response.getDateOfOrdering() != null && !response.getDateOfOrdering().isBlank()) {
            LocalDate date = LocalDate.parse(response.getDateOfOrdering(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            specification = specification.and(hasDateOfOrdering(date));
        }
        return specification;
    }

    private static Specification<Order> likeFullName(String fullName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("fio"), fullName);
    }

    private static Specification<Order> hasStatusDelivery(DeliveryStatus statusDelivery) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deliveryStatus"), statusDelivery);
    }

    private static Specification<Order> hasStatusOrder(OrderStatus statusOrder) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("orderStatus"), statusOrder);
    }

    private static Specification<Order> hasTruePayment(Boolean isPayed) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isPayed"), isPayed);
    }

    private static Specification<Order> hasTotalPrice(Integer totalPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("totalPrice"), totalPrice);
    }

    private static Specification<Order> hasTotalCount(Integer totalCount) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("countProducts"), totalCount);
    }

    private static Specification<Order> likeEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    private static Specification<Order> hasId(Long id) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Order> hasDateOfOrdering(LocalDate date) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("dateOfOrdering"), date));
    }
}
