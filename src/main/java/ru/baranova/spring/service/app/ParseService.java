package ru.baranova.spring.service.app;

import java.util.List;

public interface ParseService {
    String parseDashToNull(String str);

    List<String> parseLinesToListByComma(String str);

    Integer parseStringToInt(String str);
}
