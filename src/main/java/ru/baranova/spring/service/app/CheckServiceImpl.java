package ru.baranova.spring.service.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.domain.Entity;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService {
    private static final String correctSymbols = "[a-zA-Zа-яА-ЯёЁ\\-]+";
    private static final Pattern checkSymbols = Pattern.compile(correctSymbols);
    private static BusinessConstants.CheckServiceLog bc;

    @Override
    public Consumer<String> getLogger() {
        return log::info;
    }

    @Override
    public List<String> checkCorrectInputStrLengthAndSymbols(String str, int min, int max) {
        List<String> logList = checkCorrectInputStrLength(str, min, max);
        if (!logList.isEmpty()) {
            return logList;
        } else if (!checkSymbols.matcher(str).matches()) {
            return List.of(bc.CHAR_OR_NUMBER_INPUT);
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> checkCorrectInputStrLength(String str, int min, int max) {
        if (str == null || str.isEmpty()) {
            return List.of(bc.NOTHING_INPUT);
        } else if (str.length() < min) {
            return List.of(String.format(bc.SHORT_INPUT, min));
        } else if (str.length() >= max) {
            return List.of(String.format(bc.LONG_INPUT, max));
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> checkCorrectInputInt(Integer i) {
        return i == null ? List.of(bc.NOTHING_INPUT) : Collections.emptyList();
    }

    @Override
    public List<String> checkAllFieldsAreNotNull(Object obj) {
        if (obj == null) {
            return List.of(bc.NOTHING_INPUT);
        }
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.get(obj) == null) {
                    return List.of(String.format(bc.NOTHING_INPUT, field));
                }
            } catch (IllegalAccessException e) {
                return List.of(e.getMessage());
            }
        }
        return Collections.emptyList();
    }

    public List<String> checkExist(Entity entity) {
        return entity == null ? Collections.emptyList() : List.of(bc.SHOULD_EXIST_INPUT);
    }

    public List<String> checkIfNotExist(Supplier<List<? extends Entity>> supplier) {
        return supplier.get().isEmpty() ? Collections.emptyList() : List.of(bc.WARNING_EXIST);
    }
}
