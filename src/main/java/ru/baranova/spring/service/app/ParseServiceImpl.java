package ru.baranova.spring.service.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ParseServiceImpl implements ParseService {
    @Nullable
    @Override
    public String parseDashToNull(String str) {
        return str != null && str.equals("-") ? null : str;
    }

    @Nullable
    @Override
    public List<String> parseLinesToListByComma(@NonNull String str) {
        Pattern separator = Pattern.compile(",");
        return separator.splitAsStream(str)
                .filter(i -> i.length() > 0)
                .toList();
    }

    @Override
    public Integer parseStringToInt(@NonNull String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
