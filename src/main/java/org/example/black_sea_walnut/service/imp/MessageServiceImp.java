package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.service.MessageService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImp implements MessageService {
    private final MessageSource messageSource;
    @Override
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
