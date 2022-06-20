package ru.baranova.spring.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.baranova.spring.dao.io.InputDaoReader;
import ru.baranova.spring.dao.io.OutputDaoConsole;

@Data
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    private final InputDaoReader reader;
    private final OutputDaoConsole console;

    @Override
    public void echo() {
        String string = getReader().inputLine();
        getConsole().outputLine(string);
    }
}
