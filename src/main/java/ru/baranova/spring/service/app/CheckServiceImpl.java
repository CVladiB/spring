package ru.baranova.spring.service.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.baranova.spring.domain.BusinessConstants;

import java.lang.reflect.Field;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService {
    private static final String correctSymbols = "[a-zA-Zа-яА-ЯёЁ\\-]+";
    public static final Pattern checkSymbols = Pattern.compile(correctSymbols);
    private final AppService appServiceImpl;

    @Override
    public boolean isCorrectSymbolsInInputString(String str, int min, int max) {
        boolean isCorrect = false;
        if (isCorrectInputString(str, min, max)) {
            Matcher matcher = checkSymbols.matcher(str);
            isCorrect = matcher.matches();
            if (!isCorrect) {
                log.info(String.format(BusinessConstants.CheckServiceLog.CHAR_OR_NUMBER_INPUT));
            }
        }
        return isCorrect;
    }

    @Override
    public boolean isCorrectInputString(String str, int min, int max) {
        boolean isCorrect = false;
        if (str != null && !str.isEmpty()) {
            if (str.length() >= min) {
                if (str.length() < max) {
                    isCorrect = true;
                } else {
                    log.info(String.format(BusinessConstants.CheckServiceLog.LONG_INPUT, max));
                }
            } else {
                log.info(String.format(BusinessConstants.CheckServiceLog.SHORT_INPUT, min));
            }
        } else {
            log.info(BusinessConstants.CheckServiceLog.NOTHING_INPUT);
        }
        return isCorrect;
    }

    @Override
    public boolean isCorrectInputInt(Integer i) {
        boolean isCorrect = i != null;
        if (!isCorrect) {
            log.info(BusinessConstants.CheckServiceLog.NOTHING_INPUT);
        }
        return isCorrect;
    }

    @Override
    public boolean isAllFieldsNotNull(Object obj) {
        if (obj == null) {
            return false;
        }
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.get(obj) == null) {
                    log.info("{} {}", BusinessConstants.CheckServiceLog.NOTHING_INPUT, field);
                    return false;
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
                return false;
            }
        }
        return true;
    }

    public boolean checkExist(Supplier<Boolean> supplier) {
        boolean isExist = appServiceImpl.evaluate(supplier);
        if (!isExist) {
            log.info(BusinessConstants.CheckServiceLog.SHOULD_EXIST_INPUT);
        }
        return isExist;
    }

    public boolean checkIfNotExist(Supplier<Boolean> supplier) {
        boolean isExist = appServiceImpl.evaluate(supplier);
        if (!isExist) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_EXIST);
        }
        return isExist;
    }

}
