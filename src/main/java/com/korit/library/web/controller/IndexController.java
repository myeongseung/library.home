package com.korit.library.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    //2가지 요청 주소 전부다 쓰겠다.
    //http://localhost:8000/index
    //http://localhost:8000
    @GetMapping({"", "/index"})
    public String index(){
        return "index";
    }

}
