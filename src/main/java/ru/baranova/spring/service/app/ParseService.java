package ru.baranova.spring.service.app;

import org.springframework.lang.NonNull;

import java.util.List;

public interface ParseService {
    String parseDashToNull(String str);

    List<String> parseLinesToListStrByComma(String str);

    List<Integer> parseLinesToListIntByComma(@NonNull String str);

    Integer parseStringToInt(String str);
}
