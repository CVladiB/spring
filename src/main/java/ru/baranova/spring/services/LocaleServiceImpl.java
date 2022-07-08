package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.LocaleProvider;


@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {

    private final LocaleProvider localeProviderImpl;
    private final MessageSource messageSource;

    @Override
    public String getMessage(String keyMessage) {
        return messageSource.getMessage(keyMessage, null, localeProviderImpl.getLocale());
    }

    @Override
    public String getMessage(String keyMessage, Object... args) {
        return messageSource.getMessage(keyMessage, args, localeProviderImpl.getLocale());
    }

}
