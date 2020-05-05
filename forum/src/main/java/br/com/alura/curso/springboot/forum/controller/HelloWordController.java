package br.com.alura.curso.springboot.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWordController {

    @RequestMapping("/")
    @ResponseBody
    private String open(){
        return "Helllo World";
    }
}
