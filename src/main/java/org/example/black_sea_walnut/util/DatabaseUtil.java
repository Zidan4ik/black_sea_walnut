package org.example.black_sea_walnut.util;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class DatabaseUtil {
    public <T> T findOrThrow(Long id, Function<Long, Optional<T>> findMethod, String entityName) {
        if (id == null) return null;
        return findMethod.apply(id).
                orElseThrow(() -> new EntityNotFoundException
                        (entityName + " with id: " + id + " was not found!")
                );
    }
}
