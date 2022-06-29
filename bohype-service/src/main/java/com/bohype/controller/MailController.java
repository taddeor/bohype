package com.bohype.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MailController {
    @GetMapping("/index")
    public String index(){
        return "index";
    }
}
