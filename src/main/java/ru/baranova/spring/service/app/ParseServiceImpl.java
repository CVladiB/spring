package ru.baranova.spring.service.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ParseServiceImpl implements ParseService {
    private static final String comma = ",";
    public static final Pattern separator = Pattern.compile(comma);
    private static final String dash = "-";

    @Nullable
    @Override
    public String parseDashToNull(String str) {
        return dash.equals(str) ? null : str;
    }

    @Nullable
    @Override
    public List<String> parseLinesToListStrByComma(@NonNull String str) {
        return separator.splitAsStream(str)
                .filter(i -> !i.isBlank())
                .toList();
    }

    @Nullable
    @Override
    public List<Integer> parseLinesToListIntByComma(@NonNull String str) {
        return Objects.requireNonNull(parseLinesToListStrByComma(str))
                .stream()
                .map(this::parseStringToInt)
                .collect(Collectors.toList());
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
