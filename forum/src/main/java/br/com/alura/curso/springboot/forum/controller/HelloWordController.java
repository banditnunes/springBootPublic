package br.com.alura.curso.springboot.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


public class HelloWordController {

    private String open(){
        return "Helllo World";
    }
}
