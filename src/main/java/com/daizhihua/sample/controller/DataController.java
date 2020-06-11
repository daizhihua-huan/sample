package com.daizhihua.sample.controller;

import com.daizhihua.sample.common.Contens;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DataController {
    @RequestMapping(value = "/data")
    public String getData(){
        if(Contens.flag){
            return "error";
        }
        return "index";
    }

    @RequestMapping(value = "/upload")
    public String uploda(){
        if(Contens.flag){
            return "error";
        }
        return "layui";
    }

    @RequestMapping(value = "/map")
    public String map(){
        if(Contens.flag){
            return "error";
        }
        return "map";
    }

    @RequestMapping(value = "/mapmaker")
    public String mapmaker(){
        if(Contens.flag){
            return "error";
        }
        return "mapmaker";
    }

    @RequestMapping(value = "/dats")
    public String data(){
        if(Contens.flag){
            return "error";
        }
        return "data";
    }
    @RequestMapping("/login")
    public String userLogin() {
        if(Contens.flag){
            return "error";
        }
        return "login";
    }

    @RequestMapping(value = "/404")
    public String error(){
        return "error";
    }




}
