package org.sbtitcourses.mdwiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер главной страницы при авторизации
 */
@Controller
public class MainController {

    /**
     * @return возвращает главную страницу авторизированого пользователя
     */
    @GetMapping("/main")
    public String main() {
        return "main";
    }
}
