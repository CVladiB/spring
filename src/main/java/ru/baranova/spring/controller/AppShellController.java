package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.service.app.AppService;

@RequiredArgsConstructor
@ShellComponent
public class AppShellController {

    private final AppService appService;
    private final OutputDao outputDao;

    @ShellMethod(group = "App", value = "Exit the shell. Stop application", key = {"sa"})
    public String stopApplication() {
        return appService.stopApplication() == 0 ? "Correct exit" : "Error";
    }

    @ShellMethod(group = "App", value = "Return input", key = {"echo"})
    public String echo(String str) {
        return str;
    }

    @ShellMethod(group = "App", value = "Output", key = {"out"})
    public void output(String str) {
        outputDao.output(str);
    }
}
