package org.example.black_sea_walnut.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.transaction.ResponseTransactionForView;
import org.example.black_sea_walnut.entity.Transaction;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.enums.PaymentStatus;
import org.example.black_sea_walnut.enums.PaymentType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TransactionSpecification {
    public static Specification<Transaction> getSpecification(ResponseTransactionForView entity) {
        Specification<Transaction> specification = Specification.where(null);
        if (entity.getId() != null) {
            specification = specification.and(hasId(entity.getId()));
        }
        if (entity.getCustomer() != null && !entity.getCustomer().isBlank()) {
            specification = specification.and(likeCustomer(entity.getCustomer()));
        }
        if (entity.getPhone() != null && !entity.getPhone().isBlank()) {
            specification = specification.and(likePhone(entity.getCustomer()));
        }
        if (entity.getEmail() != null && !entity.getEmail().isBlank()) {
            specification = specification.and(likeEmail(entity.getCustomer()));
        }
        if (entity.getDate() != null && !entity.getDate().isBlank()) {
            LocalDate date = LocalDate.parse(entity.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            specification = specification.and(hasDate(date));
        }
        if (entity.getTypeOfPayment() != null && !entity.getTypeOfPayment().isBlank()) {
            specification = specification.and(hasTypeOfPayment(entity.getTypeOfPayment()));
        }
        if (entity.getStatusPayment() != null && !entity.getStatusPayment().isBlank()) {
            specification = specification.and(hasStatusPayment(entity.getStatusPayment()));
        }
        return specification;
    }

    private static Specification<Transaction> hasTypeOfPayment(String typeOfPayment) {
        PaymentType paymentType = PaymentType.valueOf(typeOfPayment.toLowerCase());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentType"), paymentType);
    }

    private static Specification<Transaction> hasStatusPayment(String statusPayment) {
        PaymentStatus paymentStatus = PaymentStatus.valueOf(statusPayment);
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus);
    }

    private static Specification<Transaction> hasDate(LocalDate date) {
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("date"), startDay, endDay));
    }

    private static Specification<Transaction> hasId(Long id) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Transaction> likeCustomer(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("customer"), "%" + title + "%");
    }

    private static Specification<Transaction> likePhone(String phone) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("phone"), "%" + phone + "%");
    }

    private static Specification<Transaction> likeEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }
    public static Specification<Transaction> hasUser(User user){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user"),user));
    }
}
