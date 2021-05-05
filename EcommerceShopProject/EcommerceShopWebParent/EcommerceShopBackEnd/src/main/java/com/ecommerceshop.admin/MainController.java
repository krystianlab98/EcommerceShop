package com.ecommerceshop.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("")
    public String viewHomePage(){
        return "indexasd";
    }

    @GetMapping("/test")
    public String test(){return "test";}
}