package org.example.black_sea_walnut.dto.web.user;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.example.black_sea_walnut.service.user.Saveable;
@Builder
@Data
public class AddressDtoIndividual implements Saveable {
    private Long id;
    private Long idCountry;
    private Long idRegion;
    private Long idCity;
    private String address;

    @Override
    public void updateEntity(User user, UserMapper mapper) {
        mapper.updateEntityFromRequest(this,user);
    }
}
