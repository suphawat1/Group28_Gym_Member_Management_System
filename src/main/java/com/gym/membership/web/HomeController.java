package com.gym.membership.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // หน้าแรก: GET /
    @GetMapping("/")
    public String home() {
        return "index";
    }
}