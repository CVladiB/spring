package ru.baranova.spring.controller.app;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.baranova.spring.service.app.AppService;

@RestController
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;

    @GetMapping("/app/sa")
    public String stopApplication() {
        return appService.stopApplication() == 0 ? "Correct exit" : "Error";
    }

    @GetMapping("/app/echo/{str}")
    public String echo(@PathVariable("str") String str) {
        return str;
    }


}
