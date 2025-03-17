package org.example.black_sea_walnut.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.black_sea_walnut.dto.admin.calls.CallResponseForView;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.enums.CallStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class CallSpecification {
    public static Specification<Call> getSpecification(CallResponseForView entity) {
        Specification<Call> specification = Specification.where(null);
        if (entity.getId() != null) {
            specification = specification.and(hasId(entity.getId()));
        }
        if (entity.getPhone() != null && !entity.getPhone().isBlank()) {
            specification = specification.and(likePhone(entity.getPhone()));
        }
        if (entity.getDate() != null && !entity.getDate().isBlank()) {
            LocalDate date = LocalDate.parse(entity.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            specification = specification.and(hasDate(date));
        }
        if (entity.getStatus() != null && !entity.getStatus().isBlank()) {
            specification = specification.and(hasStatus(entity.getStatus()));
        }
        return specification;
    }

    private static Specification<Call> hasStatus(String status) {
        CallStatus callStatus = CallStatus.fromString(status);
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), callStatus);
    }


    private static Specification<Call> hasId(Long id) {
        if (id == null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Call> likePhone(String phone) {
        if (phone == null || phone.isBlank()) return null;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("phone"), "%" + phone + "%");
    }

    private static Specification<Call> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.function("DATE", LocalDate.class, root.get("registerCall")),
                        date
                );
    }
}
