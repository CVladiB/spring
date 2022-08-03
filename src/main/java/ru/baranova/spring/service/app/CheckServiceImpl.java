package ru.baranova.spring.service.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.baranova.spring.domain.BusinessConstants;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Service
public class CheckServiceImpl implements CheckService {
    @Override
    public boolean isCorrectSymbolsInInputString(String str, int min, int max) {
        boolean isCorrect = false;
        if (isCorrectInputString(str, min, max)) {
            Pattern pattern = Pattern.compile("[a-zA-Zа-яА-ЯёЁ\\-]+");
            Matcher matcher = pattern.matcher(str);
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
    public <T> boolean isInputExist(@NonNull T inputStr, @NonNull Stream<T> existStr, Boolean shouldExist) {
        boolean isExist = existStr.anyMatch(inputStr::equals);

        if (shouldExist != null && !shouldExist && isExist) {
            log.info(BusinessConstants.CheckServiceLog.WARNING_EXIST);
        } else if (shouldExist != null && shouldExist && !isExist) {
            log.info(BusinessConstants.CheckServiceLog.SHOULD_EXIST_INPUT);
        }

        return isExist;
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

}
