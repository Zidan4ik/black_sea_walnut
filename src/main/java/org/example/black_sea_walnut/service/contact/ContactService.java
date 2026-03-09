package org.example.black_sea_walnut.service.contact;

import org.example.black_sea_walnut.entity.Contact;
import org.example.black_sea_walnut.service.history.DtoResponse;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;

import java.util.List;
import java.util.function.Function;

public interface ContactService {
    Contact save(Contact entity);

    <M extends GenericsMapper> Contact save(Saveable<Contact, M> dto, M mapper);

    <R extends DtoResponse> R getDtoResponseById(Long id, Function<Contact, R> mappingFunction);

    Contact getById(Long id);

    List<Contact> getAll();
}
