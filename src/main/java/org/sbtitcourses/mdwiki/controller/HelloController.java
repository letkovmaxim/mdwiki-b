package org.sbtitcourses.mdwiki.controller;

import org.sbtitcourses.mdwiki.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    private final AdminService adminService;

    @Autowired
    public HelloController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/admin")
    public String admin() {
        adminService.doAdminStuff();
        return "admin";
    }
}
