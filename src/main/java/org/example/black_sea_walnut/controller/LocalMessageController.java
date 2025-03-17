package org.example.black_sea_walnut.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class LocalMessageController {
    private final MessageSource messageSource;
    @GetMapping("/locale/message")
    public String getTranslation(@RequestParam String key, @RequestParam String lang) {
        Locale locale = new Locale(lang);
        return messageSource.getMessage(key, null, locale);
    }
}