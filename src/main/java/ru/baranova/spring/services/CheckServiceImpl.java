package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.baranova.spring.services.message.LocaleService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService {
    private final LocaleService localeServiceImpl;

    @Override
    public int checkCorrectInputNumber(String str, int min, int max) {
        if (str == null || str.isEmpty()) {
            log.error(localeServiceImpl.getMessage("log.nothing-input"));
            return -1;
        }

        int numb;
        try {
            numb = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.error(localeServiceImpl.getMessage("log.should-number-input"));
            return -1;
        }

        if (numb > max) {
            log.error(localeServiceImpl.getMessage("log.long-input"));
            return -1;
        }
        if (numb <= min) {
            log.error(localeServiceImpl.getMessage("log.short-input"));
            return -1;
        }

        return numb;
    }

    @Override
    public boolean checkCorrectInputStr(String str, int min) {
        if (str == null || str.isEmpty()) {
            log.error(localeServiceImpl.getMessage("log.nothing-input"));
            return false;
        }
        if (str.length() < min) {
            log.error(localeServiceImpl.getMessage("log.short-input"));
            return false;
        }
        return true;
    }
}
