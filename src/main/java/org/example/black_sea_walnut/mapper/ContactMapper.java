package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.entity.Contact;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper implements GenericsMapper {
    public ContactDtoForAdd toDtoContactForAdd(Contact entity) {
        return ContactDtoForAdd
                .builder()
                .id(entity.getId())
                .phone1(entity.getPhone1())
                .phone2(entity.getPhone2())
                .email(entity.getEmail())
                .addressWork(entity.getAddressWork())
                .addressFactory(entity.getAddressFactory())
                .coordinates(entity.getCoordinates())
                .telegram(entity.getTelegram())
                .viber(entity.getViber())
                .watsapp(entity.getWhatsapp())
                .facebook(entity.getFacebook())
                .instagram(entity.getInstagram())
                .youtube(entity.getYoutube())
                .build();
    }
    public Contact toEntityContactForAdd(ContactDtoForAdd dto, Contact entity){
        entity.setId(dto.getId());
        entity.setPhone1(dto.getPhone1());
        entity.setPhone2(dto.getPhone2());
        entity.setEmail(dto.getEmail());
        entity.setAddressWork(dto.getAddressWork());
        entity.setAddressFactory(dto.getAddressFactory());
        entity.setCoordinates(dto.getCoordinates());
        entity.setTelegram(dto.getTelegram());
        entity.setViber(dto.getViber());
        entity.setWhatsapp(dto.getWatsapp());
        entity.setFacebook(dto.getFacebook());
        entity.setInstagram(dto.getInstagram());
        entity.setYoutube(dto.getYoutube());
        return entity;
    }
}
