package ru.baranova.spring.service.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParseServiceImpl implements ParseService {
    private static final String dash = "-";

    @Nullable
    @Override
    public String parseDashToNull(String str) {
        return dash.equals(str) ? null : str;
    }

}
