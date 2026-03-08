package org.example.black_sea_walnut.service.user;

import org.example.black_sea_walnut.service.history.GenericsMapper;

public interface Saveable <E,M extends GenericsMapper> {
    Long getId();

    default void updateEntity(E entity, M mapper) {
    }
}
