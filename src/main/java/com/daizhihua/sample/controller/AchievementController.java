package com.daizhihua.sample.controller;

import com.daizhihua.sample.core.Resut;
import com.daizhihua.sample.dao.DataMapper;
import com.daizhihua.sample.dao.DicMapper;
import com.daizhihua.sample.dao.GradeCodeMapper;
import com.daizhihua.sample.dao.ImgTypeMapper;
import com.daizhihua.sample.entity.Dict;
import com.daizhihua.sample.entity.GradeCode;
import com.daizhihua.sample.entity.ImgTypeEntity;
import com.daizhihua.sample.util.NumberUtil;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;

@RestController
@Log4j2
public class AchievementController {

    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private DicMapper dicMapper;

    @Autowired
    private GradeCodeMapper gradeCodeMapper;

    @Autowired
    private ImgTypeMapper imgTypeMapper;

    /**
     * 行政区查询
     * @param type
     * @param year
     * @return
     */
    @RequestMapping(value = "/getDataForByType")
    public Resut getDataForByType(String type,String year){
        List<Map<String, Object>> list = dataMapper.getCount(type,year);
        log.info("查询的集合是"+list);
        float typeCount = dataMapper.getTypeCount(type,year);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        log.info(typeCount);

        for (Map<String, Object> map : list) {
            float y = Integer.parseInt(map.get("y").toString());
            map.put("number",  df.format( (y/typeCount)* 100.00) + "%");
        }
        return Resut.ok(list);
    }

    /**
     *
     * @param type
     * @param year
     * @return
     */
    @RequestMapping(value = "/getDataForByTypeNutrient")
    public Resut getDataForByTypeNutrient(String type,String year){
        List<Map<String, Object>> list = dataMapper.getNutrientCount(type, year);
        log.info(list);

        return Resut.ok(list);
    }

    /**
     *
     * @param type
     * @param year
     * @return
     */
    @RequestMapping(value = "/getDataForByEnvironmentContainer")
    public Resut getDataForByEnvironmentContainer(String type,String year){
        return Resut.ok(dataMapper.getEnvironmentContainer(type,year));
    }


    /**
     * 土地利用类型
     * @param type
     * @param year
     * @return
     */
    @RequestMapping(value = "/getLandTypeContainer")
    public Resut getLandTypeContainer(String type,String year){
        List<Map<String, Object>> list = dataMapper.getLandTypeContainer(type, year);
        return Resut.ok(list);
    }


    /**
     * 获取各元素的占比图
     * @param type
     * @param year
     * @param pid
     * @return
     */
    @RequestMapping(value = "/getNameCount")
    public Resut getNameCount(String type,String year,String pid){
        Map<String,Object> map = new HashMap<>();
        map.put("pid",pid);
        List<Dict> dicts = dicMapper.selectByMap(map);
        Map<String,Object>resultMap = new HashMap<>();
        /**
         * 存放名字的集合
         */
        List<String> listName = new ArrayList<>();
        int i=0;
        for (Dict dict : dicts) {
            listName.add(dict.getTitle());
            List<Map<String, Object>> nameCount = dataMapper.getNameCount(dict.getDataCode(), type, year);
            for (Map<String, Object> stringObjectMap : nameCount) {
                List<Integer> list = new ArrayList<>();
                //如果已经存在了就把存在的list取出来追加数据
                Object name = resultMap.get(stringObjectMap.get("name").toString());
                if(name!=null){
                    if(name instanceof List){
                        ((List) name).add(Integer.valueOf(stringObjectMap.get("data").toString()));
                        resultMap.put(stringObjectMap.get("name").toString(),name);
                    }
                }else{
                    if(i>0){
                        for (int i1 = 0; i1 < i; i1++) {
                            list.add(0);
                        }
                        list.add(Integer.valueOf(stringObjectMap.get("data").toString()));
                        resultMap.put(stringObjectMap.get("name").toString(),list);
                        continue;
                    }
                    list.add(Integer.valueOf(stringObjectMap.get("data").toString()));
                    resultMap.put(stringObjectMap.get("name").toString(),list);
                }

            }
            i++;
        }

        List<Map<String,Object>>listDat = new ArrayList<>();
        for (String key : resultMap.keySet()) {
            Map<String,Object> datamap = new HashMap<>();
            datamap.put("name",key);
            datamap.put("data",resultMap.get(key));
            listDat.add(datamap);
        }
        listDat.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {

                String name1 = o2.get("name").toString().substring(0,1);
                int number1 = Integer.parseInt(NumberUtil.toNumber(name1));
                log.info("name1的值"+name1.charAt(0));
                String name2 = o1.get("name").toString().substring(0,1);
                int number2 = Integer.parseInt(NumberUtil.toNumber(name2));
                log.info("name2的值"+name2);
                log.info(number2-number1);
                return  number2-number1;

            }
        });
        Map<String,Object> filallyMap = new HashMap<>();
        filallyMap.put("listName",listName);
        filallyMap.put("list",listDat);
        return Resut.ok(filallyMap);

    }


    /**
     * 查询城市的等级
      * @param type
     * @param year
     * @param city
     * @return
     */
    @RequestMapping(value = "/getNutrientForCity")
    public Resut getNutrientForCity(String type,String year,String city){
        List<Map<String, Object>> nutrientCountCity = dataMapper.getNutrientCountCity(type, year, city);
        return Resut.ok(nutrientCountCity);
    }


    @RequestMapping(value="/getEnvironmentForCity")
    public Resut getEnvironmentForCity(String type,String year,String city){
        List<Map<String, Object>> environmentContainerCity = dataMapper.getEnvironmentContainerCity(type, year, city);
        return Resut.ok(environmentContainerCity);

    }

    @RequestMapping(value = "/getLandTypeForCity")
    public Resut getLandTypeForCity(String type,String year,String city){
        List<Map<String, Object>> landTypeContainerCity = dataMapper.getLandTypeContainerCity(type, year, city);
        return Resut.ok(landTypeContainerCity);
    }


    @RequestMapping(value = "/getImageFile")
    public Resut getImageType(String pid,String type,String year){
        Map<String,Object> map = new HashMap<>();
        map.put("pid",pid);
        map.put("type",type);
        map.put("year",year);
        List<ImgTypeEntity> imgTypeEntities = imgTypeMapper.selectByMap(map);
        return Resut.ok(imgTypeEntities);
    }


}
