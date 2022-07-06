package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.baranova.spring.annotation.MethodArg;
import ru.baranova.spring.dao.LocaleProvider;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.LanguageDescription;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {
    private final OutputDao outputDaoConsole;
    private final InputDao inputDaoReader;

    private final LocaleProvider localeProviderImpl;
    private final MessageSource messageSource;

    @Override
    public String getMessage(String keyMessage) {
        return messageSource.getMessage(keyMessage, null, localeProviderImpl.getLocale());
    }

    @MethodArg
    @Override
    public String getMessage(String keyMessage, Object... args) {
        return messageSource.getMessage(keyMessage, args, localeProviderImpl.getLocale());
    }

    @Override
    public void chooseLanguage() {
        List<LanguageDescription> languages = localeProviderImpl.getLanguages();
        outputDaoConsole.outputLine("Выберите язык:");
        for (int i = 1; i <= languages.size(); i++) {
            localeProviderImpl.setLanguageDescription(languages.get(i - 1));
            outputDaoConsole.outputLine(getMessage("message.choose-language", i));
        }

        int indexLanguage;
        do {
            indexLanguage = checkAndGet(inputDaoReader.inputLine(), languages.size());
        } while (indexLanguage == -1);

        localeProviderImpl.setLanguageDescription(languages.get(indexLanguage - 1));
    }

    private int checkAndGet(String str, int maxLength) {
        if (str == null || str.isEmpty()) {
            log.info(getMessage("log.nothing-input"));
            return -1;
        }

        int numb;
        try {
            numb = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.info(getMessage("log.should-number-input"));
            return -1;
        }

        if (numb > maxLength) {
            log.info(getMessage("log.long-input"));
            return -1;
        }
        if (numb <= 0) {
            log.info(getMessage("log.short-input"));
            return -1;
        }

        return numb;
    }

}
