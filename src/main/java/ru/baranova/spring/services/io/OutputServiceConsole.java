package ru.baranova.spring.services.io;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.services.message.LocaleService;

@Service
@RequiredArgsConstructor
public class OutputServiceConsole implements OutputService {
    private final OutputDao outputDaoConsole;
    private final LocaleService localeServiceImpl;

    @Override
    public void getMessage(String keyMessage) {
        String message = localeServiceImpl.getMessage(keyMessage);
        outputDaoConsole.outputLine(message);
    }

    @Override
    public void getMessage(String keyMessage, Object... args) {
        String message = localeServiceImpl.getMessage(keyMessage, args);
        outputDaoConsole.outputLine(message);
    }
}
