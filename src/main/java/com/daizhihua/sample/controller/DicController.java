package com.daizhihua.sample.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
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

    /**
     * 获取 元素指标
     * @return
     */
    @RequestMapping("/getDicer")
    public Resut getDic(){
        log.info(dicMapper.selectByMap(null));
        Map<String,Object> map = new HashMap<>();
        map.put("pid","0");
        List<DictParten> dicts = dictPartenMapper.selectByMap(map);
        for (DictParten dict : dicts) {
            String pid = dict.getId();
            List<Dict> dicts1 = dicMapper.selectList(new QueryWrapper<Dict>()
                    .eq("pid", pid)
                    .orderBy(true,true,"sort_no"));
            dict.setChildren(dicts1);
        }
        return Resut.ok(dicts);
    }

    /**
     * 重点工区查询
     * @return
     */
    @RequestMapping(value = "/getDicImgType")
    public Resut getDicImgType(String pid){
        Map<String,Object> map = new HashMap<>();
        map.put("pid",pid);
        List<DictParten> dictPartens = dictPartenMapper.selectByMap(map);
        for (DictParten dictParten : dictPartens) {
            Map<String,Object> dicMap = new HashMap<>();
            dicMap.put("pid",dictParten.getId());
            dictParten.setChildren(dicMapper.selectByMap(dicMap));
        }
        return Resut.ok(dictPartens);

    }

    /**
     *
     * @param pid
     * @return
     */
    @RequestMapping(value = "/getDicForPid")
    public Resut getDicForPid(String pid){
        Map<String,Object> map = new HashMap<>();
        map.put("pid",pid);
        return Resut.ok(dicMapper.selectByMap(map));
    }

}
