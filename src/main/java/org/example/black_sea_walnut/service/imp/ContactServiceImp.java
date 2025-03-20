package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.entity.Contact;
import org.example.black_sea_walnut.mapper.ContactMapper;
import org.example.black_sea_walnut.repository.ContactRepository;
import org.example.black_sea_walnut.service.ContactService;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImp implements ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    @Override
    public Contact save(Contact entity) {
        LogUtil.logInfo("Saving Contact entity: " + entity);
        Contact savedContact = contactRepository.save(entity);
        LogUtil.logInfo("Saved Contact entity: " + savedContact);
        return savedContact;
    }

    @Override
    public Contact save(ContactDtoForAdd dto) {
        LogUtil.logInfo("Saving Contact from DTO: " + dto);
        Contact contact = contactMapper.toEntityContactForAdd(dto);
        Contact savedContact = save(contact);
        LogUtil.logInfo("Saved Contact from DTO: " + savedContact);
        return savedContact;
    }

    @Override
    public Contact getById(Long id) {
        LogUtil.logInfo("Fetching Contact with ID: " + id);
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> {
                    LogUtil.logError("Contact with ID: " + id + " was not found!", null);
                    return new EntityNotFoundException("Contact with id:" + id + " was not found!");
                });
        LogUtil.logInfo("Found Contact with ID: " + id);
        return contact;
    }

    @Override
    public ContactDtoForAdd getByIdInDto(Long id) {
        LogUtil.logInfo("Fetching Contact DTO for ID: " + id);
        ContactDtoForAdd dto = contactMapper.toDtoContactForAdd(getById(id));
        LogUtil.logInfo("Found Contact DTO for ID: " + id);
        return dto;
    }

    @Override
    public List<Contact> getAll() {
        LogUtil.logInfo("Fetching all Contacts.");
        List<Contact> contacts = contactRepository.findAll();
        LogUtil.logInfo("Fetched " + contacts.size() + " Contacts.");
        return contacts;
    }
}
