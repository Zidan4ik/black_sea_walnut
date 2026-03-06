package org.example.black_sea_walnut.service.user;

public interface Saveable <E,M> {
    Long getId();

    default void updateEntity(E entity, M mapper) {
    }
}
