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

    @RequestMapping(value = "/map")
    public String map(){
        return "map";
    }

    @RequestMapping(value = "/mapmaker")
    public String mapmaker(){
        return "mapmaker";
    }

    @RequestMapping(value = "/dats")
    public String data(){
        return "data";
    }





}
