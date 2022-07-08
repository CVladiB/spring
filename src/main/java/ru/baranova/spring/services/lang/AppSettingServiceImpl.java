package ru.baranova.spring.services.lang;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.LocaleProvider;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.LanguageDescription;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.io.OutputService;

@Service
@RequiredArgsConstructor
public class AppSettingServiceImpl implements AppSettingService {
    private final OutputDao outputDaoConsole;
    private final InputDao inputDaoReader;
    private final CheckService checkServiceImpl;
    private final LocaleProvider localeProviderImpl;
    private final OutputService outputServiceConsole;

    @Override
    public void printOptionsOfLanguage() {
        outputDaoConsole.outputLine("Выберите язык:");
        int count = 0;
        for (LanguageDescription language : localeProviderImpl.getLanguages()) {
            localeProviderImpl.setLanguageDescription(language);
            outputServiceConsole.getMessage("message.choose-language", ++count);
        }
    }

    @Override
    public void setLanguage() {
        int indexLanguage;
        do {
            indexLanguage = checkServiceImpl.checkCorrectInputNumber(inputDaoReader.inputLine(), 0, localeProviderImpl.getLanguages().size());
        } while (indexLanguage == -1);

        localeProviderImpl.setLanguageDescription(localeProviderImpl.getLanguages().get(indexLanguage - 1));
    }

}
