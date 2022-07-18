package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.baranova.spring.services.shell.ShellService;

@ShellComponent
@RequiredArgsConstructor
public class ShellController {
    private final ShellService shellServiceImpl;


    @ShellMethod(value = "App language setting command", key = {"l", "language"})
    public void chooseLanguage() {
        shellServiceImpl.chooseLanguage();
    }

    @ShellMethod(value = "Test command", key = {"t", "test"})
    public void test() {
        shellServiceImpl.test();
    }

}
