package org.example.black_sea_walnut.service.user;

import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.springframework.web.multipart.MultipartFile;

public interface UserProcessable extends UserUpdater{
    Long getId();
    MultipartFile getFileImage();
    String getPathToImage();
    void setPathToImage(String path);
    void updateEntity(User user, UserMapper mapper);
}
