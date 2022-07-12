package ru.baranova.spring.services.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.LocaleProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {

    private final LocaleProvider localeProviderImpl;
    private final MessageSource messageSource;

    @Override
    public String getMessage(String keyMessage) {
        String message = "Ошибка загрузки сообщения, обратитесь к администратору";
        try {
            message = messageSource.getMessage(keyMessage, null, localeProviderImpl.getLocale());
        } catch (NoSuchMessageException e) {
            log.error(messageSource.getMessage("log.wrong-load-message", null, localeProviderImpl.getLocale()));
            try {
                message = messageSource.getMessage("log.wrong-load-message", null, localeProviderImpl.getLocale());
            } catch (Exception ex) {
                log.error(message);
            }
        }
        return message;
    }

    @Override
    public String getMessage(String keyMessage, Object... args) {
        String message = "Ошибка загрузки сообщения, обратитесь к администратору";
        try {
            message = messageSource.getMessage(keyMessage, args, localeProviderImpl.getLocale());
        } catch (NoSuchMessageException e) {
            log.error(messageSource.getMessage("log.wrong-load-message", null, localeProviderImpl.getLocale()));
            try {
                message = messageSource.getMessage("log.wrong-load-message", null, localeProviderImpl.getLocale());
            } catch (Exception ex) {
                log.error(message);
            }
        }
        return message;
    }
}
