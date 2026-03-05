package org.example.black_sea_walnut.service.user;

import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.mapper.UserMapper;

public interface Saveable {
    Long getId();
    void updateEntity(User user, UserMapper mapper);
}
