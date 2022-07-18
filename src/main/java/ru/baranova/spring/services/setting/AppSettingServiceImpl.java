package ru.baranova.spring.services.setting;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private AppSettingService self;

    @Override
    public void chooseLanguage() {
        self.printOptionsOfLanguage();
        int indexLanguage = self.inputNumberOfLanguage();
        self.setLanguage(indexLanguage);
    }

    @Override
    public void printOptionsOfLanguage() {
        outputDaoConsole.outputLine("Выберите язык:");
        int count = 0;
        for (LanguageDescription language : localeProviderImpl.getLanguages()) {
            localeProviderImpl.setLanguageDescription(language);
            outputServiceConsole.getMessage("message.choose-language", ++count);
        }
        outputDaoConsole.outputFormatLine(":> ");
    }

    @Override
    public int inputNumberOfLanguage() {
        int indexLanguage;
        do {
            String inputAnswer = inputDaoReader.inputLine();
            int minInputNumber = 0;
            int maxInputNumber = localeProviderImpl.getLanguages().size();
            indexLanguage = checkServiceImpl.checkCorrectInputNumber(inputAnswer, minInputNumber, maxInputNumber);
        } while (indexLanguage == -1);
        return indexLanguage;
    }

    @Override
    public void setLanguage(int indexLanguage) {
        LanguageDescription languageDescriptionByIndex = localeProviderImpl.getLanguages().get(indexLanguage - 1);
        localeProviderImpl.setLanguageDescription(languageDescriptionByIndex);
    }
}
