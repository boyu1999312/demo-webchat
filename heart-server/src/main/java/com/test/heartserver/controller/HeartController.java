package com.test.heartserver.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/server")
public class HeartController {


    @GetMapping("/heart")
    public String check(){

        return "嘻嘻";
    }
}
