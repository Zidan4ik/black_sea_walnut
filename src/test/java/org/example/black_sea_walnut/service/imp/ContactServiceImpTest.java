package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.entity.Contact;
import org.example.black_sea_walnut.mapper.ContactMapper;
import org.example.black_sea_walnut.repository.ContactRepository;
import org.example.black_sea_walnut.service.imp.ContactServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImpTest {
    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactMapper contactMapper;

    @InjectMocks
    private ContactServiceImp contactService;

    private Contact contact;
    private ContactDtoForAdd contactDto;

    @BeforeEach
    void setUp() {
        contact = new Contact();
        contact.setId(1L);
        contact.setEmail("Test Email");
        contactDto = ContactDtoForAdd.builder().build();
        contactDto.setEmail("Test Email DTO");
    }

    @Test
    void testSaveEntity() {
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        Contact savedContact = contactService.save(contact);
        assertNotNull(savedContact);
        assertEquals(contact.getId(), savedContact.getId());
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void testSaveDto() {
        when(contactMapper.toEntityContactForAdd(any(ContactDtoForAdd.class))).thenReturn(contact);
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        Contact savedContact = contactService.save(contactDto);
        assertNotNull(savedContact);
        assertEquals(contact.getId(), savedContact.getId());
        verify(contactMapper, times(1)).toEntityContactForAdd(contactDto);
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void testGetById_Found() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        Contact foundContact = contactService.getById(1L);
        assertNotNull(foundContact);
        assertEquals(contact.getId(), foundContact.getId());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> contactService.getById(1L));
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdInDto() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(contactMapper.toDtoContactForAdd(contact)).thenReturn(contactDto);
        ContactDtoForAdd foundDto = contactService.getByIdInDto(1L);
        assertNotNull(foundDto);
        assertEquals(contactDto.getEmail(), foundDto.getEmail());
        verify(contactRepository, times(1)).findById(1L);
        verify(contactMapper, times(1)).toDtoContactForAdd(contact);
    }

    @Test
    void testGetAll() {
        when(contactRepository.findAll()).thenReturn(List.of(contact));
        List<Contact> contacts = contactService.getAll();
        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());
        assertEquals(1, contacts.size());
        verify(contactRepository, times(1)).findAll();
    }
}