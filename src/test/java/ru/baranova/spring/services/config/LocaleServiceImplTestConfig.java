package ru.baranova.spring.services.config;


import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.baranova.spring.dao.LocaleProvider;
import ru.baranova.spring.services.LocaleService;
import ru.baranova.spring.services.LocaleServiceImpl;

@TestConfiguration
public class LocaleServiceImplTestConfig {

    @Bean
    public LocaleService testLocaleServiceImpl(LocaleProvider testLocaleProviderImpl, ReloadableResourceBundleMessageSource messageSource) {
        return new LocaleServiceImpl(testLocaleProviderImpl, messageSource);
    }

    @Bean
    public LocaleProvider testLocaleProviderImpl() {
        return Mockito.mock(LocaleProvider.class);
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

}
