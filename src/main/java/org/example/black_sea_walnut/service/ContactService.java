package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.entity.Contact;

import java.util.List;

public interface ContactService {
    Contact save(Contact entity);

    Contact save(ContactDtoForAdd dto);

    Contact getById(Long id);

    ContactDtoForAdd getByIdInDto(Long id);

    List<Contact> getAll();
}
