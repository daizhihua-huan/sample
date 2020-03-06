package com.daizhihua.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DataController {
    @RequestMapping(value = "/data")
    public String getData(){
        return "index";
    }

    @RequestMapping(value = "/upload")
    public String uploda(){
        return "layui";
    }
}
