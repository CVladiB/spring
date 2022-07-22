package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Controller;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.service.app.AppService;

@RequiredArgsConstructor
@Controller
@ShellComponent
public class AppShellController {

    private final AppService appServiceImpl;
    private final OutputDao outputDaoConsole;

    @ShellMethod(group = "App", value = "Exit the shell. Stop application", key = {"sa"})
    public void stopApplication() {
        appServiceImpl.stopApplication();
    }

    @ShellMethod(group = "App", value = "Return input", key = {"echo"})
    public String echo(String str) {
        return str;
    }

    @ShellMethod(group = "App", value = "Output", key = {"out"})
    public void output(String str) {
        outputDaoConsole.output(str);
    }
}
