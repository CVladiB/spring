package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService {
    private final LocaleService localeServiceImpl;

    @Override
    public int checkCorrectInputNumber(String str, int min, int max) {
        if (str == null || str.isEmpty()) {
            log.info(localeServiceImpl.getMessage("log.nothing-input"));
            return -1;
        }

        int numb;
        try {
            numb = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.info(localeServiceImpl.getMessage("log.should-number-input"));
            return -1;
        }

        if (numb > max) {
            log.info(localeServiceImpl.getMessage("log.long-input"));
            return -1;
        }
        if (numb <= min) {
            log.info(localeServiceImpl.getMessage("log.short-input"));
            return -1;
        }

        return numb;
    }

    @Override
    public boolean checkCorrectInputStr(String str, int min) {
        if (str == null || str.isEmpty()) {
            log.info(localeServiceImpl.getMessage("log.nothing-input"));
            return false;
        }
        if (str.length() < min) {
            log.info(localeServiceImpl.getMessage("log.short-input"));
            return false;
        }
        return true;
    }
}
