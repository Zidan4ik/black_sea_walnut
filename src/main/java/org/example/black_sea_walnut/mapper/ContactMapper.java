package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {
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
    public Contact toEntityContactForAdd(ContactDtoForAdd dto){
        return Contact
                .builder()
                .id(dto.getId())
                .phone1(dto.getPhone1())
                .phone2(dto.getPhone2())
                .email(dto.getEmail())
                .addressWork(dto.getAddressWork())
                .addressFactory(dto.getAddressFactory())
                .coordinates(dto.getCoordinates())
                .telegram(dto.getTelegram())
                .viber(dto.getViber())
                .whatsapp(dto.getWatsapp())
                .facebook(dto.getFacebook())
                .instagram(dto.getInstagram())
                .youtube(dto.getYoutube())
                .build();
    }
}
