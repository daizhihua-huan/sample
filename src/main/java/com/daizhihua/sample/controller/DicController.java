package com.daizhihua.sample.controller;

import com.daizhihua.sample.core.Resut;
import com.daizhihua.sample.dao.DicMapper;
import com.daizhihua.sample.dao.DictPartenMapper;
import com.daizhihua.sample.entity.Dict;
import com.daizhihua.sample.entity.DictParten;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Log4j2
public class DicController {


    @Autowired
    private DicMapper dicMapper;

    @Autowired
    private DictPartenMapper dictPartenMapper;

    @RequestMapping("/getDicer")
    public Resut getDic(){
        log.info(dicMapper.selectByMap(null));
        Map<String,Object> map = new HashMap<>();
        map.put("pid","0");
        List<DictParten> dicts = dictPartenMapper.selectByMap(map);
        for (DictParten dict : dicts) {
            Map<String,Object> dicmap = new HashMap<>();
            String pid = dict.getId();
            dicmap.put("pid",pid);
            dict.setChildren(dicMapper.selectByMap(dicmap));
        }
        return Resut.ok(dicts);
    }

    @RequestMapping(value = "/getDicForPid")
    public Resut getDicForPid(String pid){
        Map<String,Object> map = new HashMap<>();
        map.put("pid",pid);
        return Resut.ok(dictPartenMapper.selectByMap(map));
    }

}
