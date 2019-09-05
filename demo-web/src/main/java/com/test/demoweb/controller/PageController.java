package com.test.demoweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/html")
public class PageController {

    @RequestMapping("/login")
    public String login(){

        return "login";
    }
}
