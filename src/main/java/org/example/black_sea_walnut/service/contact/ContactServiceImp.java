package org.example.black_sea_walnut.service.contact;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.Contact;
import org.example.black_sea_walnut.repository.ContactRepository;
import org.example.black_sea_walnut.service.history.DtoResponse;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ContactServiceImp implements ContactService {
    private final ContactRepository contactRepository;

    @Override
    public <M extends GenericsMapper> Contact save(Saveable<Contact, M> dto, M mapper) {
        Contact contact = (dto.getId() != null) ? getById(dto.getId()) : new Contact();
        dto.updateEntity(contact, mapper);
        return save(contact);
    }

    @Override
    public <R extends DtoResponse> R getDtoResponseById(Long id, Function<Contact, R> mappingFunction) {
        Contact contactById = getById(id);
        return mappingFunction.apply(contactById);
    }

    @Override
    public Contact save(Contact entity) {
        LogUtil.logInfo("Saving Contact entity: " + entity);
        Contact savedContact = contactRepository.save(entity);
        LogUtil.logInfo("Saved Contact entity: " + savedContact);
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
    public List<Contact> getAll() {
        LogUtil.logInfo("Fetching all Contacts.");
        List<Contact> contacts = contactRepository.findAll();
        LogUtil.logInfo("Fetched " + contacts.size() + " Contacts.");
        return contacts;
    }
}
