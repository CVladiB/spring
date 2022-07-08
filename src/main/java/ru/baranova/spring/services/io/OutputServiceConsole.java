package ru.baranova.spring.services.io;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.services.LocaleService;

@Service
@RequiredArgsConstructor
public class OutputServiceConsole implements OutputService {
    private final OutputDao outputDaoConsole;
    private final LocaleService localeServiceImpl;

    @Override
    public void getMessage(String keyMessage) {
        outputDaoConsole.outputLine(localeServiceImpl.getMessage(keyMessage));
    }

    @Override
    public void getMessage(String keyMessage, Object... args) {
        outputDaoConsole.outputLine(localeServiceImpl.getMessage(keyMessage, args));
    }
}
