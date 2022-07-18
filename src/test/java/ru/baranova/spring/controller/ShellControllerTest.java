package ru.baranova.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.baranova.spring.services.shell.ShellService;


@SpringBootTest
class ShellControllerTest {

    String language;
    String shortLanguage;
    String test;
    String shortTest;
    String echo;
    String shortEcho;
    String stopApplication;
    String shortStopApplication;
    @Autowired
    private Shell shell;
    @MockBean
    private ShellService shellServiceImpl;

    @BeforeEach
    void setUp() {
        language = "language";
        shortLanguage = "l";
        test = "test";
        shortTest = "t";
        echo = "echo";
        shortEcho = "e";
        stopApplication = "stopApp";
        shortStopApplication = "sa";
    }


    @Test
    void chooseLanguage_correctKey() {
        shell.evaluate(() -> language);
        Mockito.verify(shellServiceImpl).chooseLanguage();
    }

    @Test
    void chooseLanguage_correctShortKey() {
        shell.evaluate(() -> shortLanguage);
        Mockito.verify(shellServiceImpl).chooseLanguage();
    }

    @Test
    void chooseLanguage_incorrectKey() {
        shell.evaluate(() -> "smthWrong");
        Mockito.verify(shellServiceImpl, Mockito.never()).chooseLanguage();
    }

    @Test
    void test_correctKey() {
        shell.evaluate(() -> test);
        Mockito.verify(shellServiceImpl).test();
    }

    @Test
    void test_correctShortKey() {
        shell.evaluate(() -> shortTest);
        Mockito.verify(shellServiceImpl).test();
    }

    @Test
    void test_incorrectKey() {
        shell.evaluate(() -> "smthWrong");
        Mockito.verify(shellServiceImpl, Mockito.never()).test();
    }

    @Test
    void echo_correctKey() {
        shell.evaluate(() -> echo + " smthParameter");
        Mockito.verify(shellServiceImpl).echo("smthParameter");
    }

    @Test
    void echo_correctShortKey() {
        shell.evaluate(() -> echo + " smthParameter");
        Mockito.verify(shellServiceImpl).echo("smthParameter");
    }

    @Test
    void echo_incorrectKey() {
        shell.evaluate(() -> "smthWrong");
        Mockito.verify(shellServiceImpl, Mockito.never()).echo("smthWrong");
    }

    @Test
    void stopApplication_correctKey() {
        shell.evaluate(() -> stopApplication);
        Mockito.verify(shellServiceImpl).stopApplication();
    }

    @Test
    void stopApplication_correctShortKey() {
        shell.evaluate(() -> shortStopApplication);
        Mockito.verify(shellServiceImpl).stopApplication();
    }

    @Test
    void stopApplication_incorrectKey() {
        shell.evaluate(() -> "smthWrong");
        Mockito.verify(shellServiceImpl, Mockito.never()).stopApplication();
    }
}