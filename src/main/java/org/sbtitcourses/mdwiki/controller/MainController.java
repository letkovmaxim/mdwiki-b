package org.sbtitcourses.mdwiki.controller;

/**
 * Контроллер главной страницы при авторизации
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
