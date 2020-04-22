package com.daizhihua.sample.controller;


import com.daizhihua.sample.core.Resut;
import com.daizhihua.sample.dao.*;
import com.daizhihua.sample.entity.DataEntity;
import com.daizhihua.sample.entity.GradeCode;
import com.daizhihua.sample.entity.PointData;
import com.daizhihua.sample.entity.RegionData;
import com.daizhihua.sample.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@Slf4j
public class FileController {

    @Autowired
    TestMapper testMapper;

    @Autowired
    private DataMapper dataMapper;
    
    @Autowired
    private GradeCodeMapper gradeCodeMapper;



    @RequestMapping(value = "/sendFile",method = RequestMethod.POST)
    @ResponseBody
//    @Transactional(readOnly = false)
//    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRED)
    public Resut sendFile(@RequestParam(name = "file") MultipartFile multipartFile){
        log.info(multipartFile.getName());
        log.info(multipartFile.getOriginalFilename());
        log.info(multipartFile.getContentType());
        String name = multipartFile.getOriginalFilename();
        if(name.indexOf(".")==-1){
            return Resut.error("上传文件类型错误");
        }

        try {
            List<DataEntity> dataEntities = ExcelUtils.readExcelToEntity(DataEntity.class, multipartFile.getInputStream(), name);
            System.out.println(dataEntities);
            for (DataEntity dataEntity : dataEntities) {
                if(dataEntity.getId()==null){
                    continue;
                }
                //dataEntity.setDataId(0L);
                System.out.println(dataEntity);
                dataMapper.insert(dataEntity);
                /*if(dataEntity.getType().equals("区域监测")){
                    RegionData regionData = new RegionData();
                    BeanUtils.copyProperties(dataEntity, regionData);
                    regionDataMapper.insert(regionData);
                }else{
                    PointData pointData = new PointData();
                    BeanUtils.copyProperties(dataEntity,pointData);
                    pointDataMapper.insert(pointData);
                }*/
//                dataMapper.insert(dataEntity);
            }
//            dataMapper.saveAll(dataEntities);
            log.info("数据是"+dataEntities);
//            ExcleUtile.analisy(name.substring(name.indexOf(".")+1),multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return Resut.error("文件模板错误");
        } catch (Exception e) {
            e.printStackTrace();
            return Resut.error("文件模板错误");
        }
//        multipartFile.getInputStream()

        return Resut.ok();

    }

    /**
     * 1.通过数据字典获取类型
     * 2.类型获取对应元素
     * 3.
     * @param
     * @return
     */
    @RequestMapping(value = "/getData")
    @ResponseBody
    public Resut getData(String name, int index,String type,String year) throws IllegalAccessException {
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("years",year);
        List<DataEntity> all = dataMapper.selectByMap(map);
        Map<String,Object> mapAll = new HashMap<>();
        int style1index = 0;
        List<GradeCode> listName = new ArrayList<>();
        List<GradeCode> gradeCodes = gradeCodeMapper.selectByMap(null);
        for (DataEntity dataEntity : all) {
            String longitude = dataEntity.getLongitude();
            String latitude = dataEntity.getLatitude();
            List<String> list = new ArrayList<>();
            list.add(longitude);
            list.add(latitude);
            Field[] fields = dataEntity.getClass().getDeclaredFields();
            fields[index].setAccessible(true);
            if(fields[index].getName().equals(name)){
                for (GradeCode gradeCode : gradeCodes) {
                    if(fields[index].get(dataEntity).equals(gradeCode.getName())){
                        dataEntity.setStyle(Integer.parseInt(gradeCode.getIndexCode()));
                        if (listName.indexOf(gradeCode)==-1){
                            listName.add(gradeCode);
                        }

                    }
                }


               /* if(fields[index].get(dataEntity).equals("一级")){
                    dataEntity.setStyle(0);
                    listName.add(fields[index].get(dataEntity).toString());
                }else if(fields[index].get(dataEntity).equals("二级")){
                    dataEntity.setStyle(1);
                    listName.add(fields[index].get(dataEntity).toString());
                }else if(fields[index].get(dataEntity).equals("三级")){
                    dataEntity.setStyle(2);
                    listName.add(fields[index].get(dataEntity).toString());
                }else if(fields[index].get(dataEntity).equals("四级")){
                    dataEntity.setStyle(3);
                    listName.add(fields[index].get(dataEntity).toString());
                }else{
                    dataEntity.setStyle(4);
                    listName.add(fields[index].get(dataEntity).toString());
                }*/
            }
            dataEntity.setLnglat(list);
        }
        Collections.sort(listName);
        mapAll.put("data",all);
        mapAll.put("listName",listName);
        return Resut.ok(mapAll);
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public Resut test(){
//        DataEntity dataEntity = new DataEntity();
//        dataEntity.setId("123");
//        dataMapper.save(dataEntity);
        //testMapper.findByState()
        return Resut.ok(testMapper.findByState());
    }

    @GetMapping(value = "/getVideos")
    public String getVideos(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            FileInputStream fis = null;
            OutputStream os = null ;
            fis = new FileInputStream("D:\\test\\localvideo\\out.mp4");
            int size = fis.available(); // 得到文件大小
            byte data[] = new byte[size];
            fis.read(data); // 读数据
            fis.close();
            fis = null;
            response.setContentType("video/mp4"); // 设置返回的文件类型
            os = response.getOutputStream();
            os.write(data);
            os.flush();
            os.close();
            os = null;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
