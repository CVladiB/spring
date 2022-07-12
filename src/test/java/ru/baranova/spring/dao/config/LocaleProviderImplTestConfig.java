package ru.baranova.spring.dao.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.LocaleProvider;
import ru.baranova.spring.dao.LocaleProviderImpl;
import ru.baranova.spring.domain.LanguageDescription;

import java.util.List;

@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "app.dao.locale-provider")
public class LocaleProviderImplTestConfig {
    private List<LanguageDescription> languages;
    private LanguageDescription languageDescription;

    @Bean
    public LocaleProvider testLocaleProviderImpl() {
        LocaleProviderImpl localeProvider = new LocaleProviderImpl(languages);
        localeProvider.setLanguageDescription(languageDescription);
        return localeProvider;
    }

}
