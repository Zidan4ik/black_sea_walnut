package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.entity.Contact;
import org.example.black_sea_walnut.mapper.ContactMapper;
import org.example.black_sea_walnut.repository.ContactRepository;
import org.example.black_sea_walnut.service.ContactService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImp implements ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    @Override
    public Contact save(Contact entity) {
        return contactRepository.save(entity);
    }

    @Override
    public Contact save(ContactDtoForAdd dto) {
        return save(contactMapper.toEntityContactForAdd(dto));
    }

    @Override
    public Contact getById(Long id) {
        return contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contact with id:" + id + " was not found!"));
    }

    @Override
    public ContactDtoForAdd getByIdInDto(Long id) {
        return contactMapper.toDtoContactForAdd(getById(id));
    }
}
