package ru.baranova.spring.dao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.baranova.spring.domain.LanguageDescription;

import java.util.List;
import java.util.Locale;


@Getter
@Setter
@RequiredArgsConstructor
@Component
@ConfigurationProperties(prefix = "app.dao.locale-provider")
public class LocaleProviderImpl implements LocaleProvider {
    private final List<LanguageDescription> languages;
    private LanguageDescription languageDescription;

    @Override
    public Locale getLocale() {
        return Locale.forLanguageTag(languageDescription.getLanguage());
    }
}
