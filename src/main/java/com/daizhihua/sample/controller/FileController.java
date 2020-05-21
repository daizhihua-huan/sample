package com.daizhihua.sample.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.daizhihua.sample.core.Resut;
import com.daizhihua.sample.dao.*;
import com.daizhihua.sample.entity.*;
import com.daizhihua.sample.util.DateUtil;
import com.daizhihua.sample.util.ExcelUtils;
import com.daizhihua.sample.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
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

    @Autowired
    private ImgTypeMapper imgTypeMapper;

    private List<DataEntity> list;


    @RequestMapping(value = "/getImage")
    public void getImage(String type,String year,HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("year",year);
        List<ImgTypeEntity> imgTypeEntities = imgTypeMapper.selectByMap(map);
        if(imgTypeEntities.size()<=0){
            InputStream in = this.getClass().getResourceAsStream("/static/images/noneshow.png");

           return;
        }
        ImgTypeEntity imgTypeEntity = imgTypeEntities.get(0);
        String path = System.getProperty("user.dir")+"\\"+imgTypeEntity.getPath();
        File file = new File(path);
        getByte(file,response);

    }


    @RequestMapping(value = "/getImageFileType")
    public void getImage(String path,HttpServletResponse response){
        String filePath = System.getProperty("user.dir")+"\\"+path;
        File file = new File(filePath);
        if(!file.exists()){
            return;
        }
        getByte(file,response);

    }



    public void getByte(File file,HttpServletResponse response){
        response.setContentType("image/gif");
        FileInputStream fis =null;
        try {
            OutputStream out = response.getOutputStream();
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 上传图片文件
     * @param multipartFile
     * @param type
     * @param year
     * @return
     */
    @RequestMapping(value = "/sendImageFile",method = RequestMethod.POST)
    @ResponseBody
    public Resut sendImageFile(@RequestParam(name = "image")MultipartFile multipartFile,String type,String year,String pid,String imgType){

        if(multipartFile==null){
            return Resut.error("文件为空请选择上传的文件");
        }
        String path = "upload"+"\\"+year+"\\"+pid;
        String filePath = System.getProperty("user.dir")+"\\"+path;
        File file = new File(filePath);
        if(!file.exists()){
            log.info("文件不存在的话就创建");
            file.mkdirs();
        }
        String fileName = multipartFile.getOriginalFilename();
        File dest = new File(filePath +"\\"+ fileName);

        try {
            multipartFile.transferTo(dest);
            log.info("上传成功");
            Map<String,Object>map = new HashMap<>();
            map.put("year",year);
            map.put("type",type);
            map.put("pid",pid);
            map.put("imgType",imgType);
            List<ImgTypeEntity> imgTypeEntities = imgTypeMapper.selectByMap(map);
            if(imgTypeEntities.size()>0){
                ImgTypeEntity imgTypeEntity = imgTypeEntities.get(0);
                imgTypeEntity.setType(type);
                imgTypeEntity.setName(fileName);
                imgTypeEntity.setYear(year);
                imgTypeEntity.setPath(path+"\\"+fileName);
                imgTypeEntity.setPid(pid);
                imgTypeEntity.setImgType(imgType);
                imgTypeMapper.updateById(imgTypeEntity);
            }else{
                ImgTypeEntity imgTypeEntity = new ImgTypeEntity();
                imgTypeEntity.setName(fileName);
                imgTypeEntity.setPath(path+"\\"+fileName);
                imgTypeEntity.setCreateTime(DateUtil.getNewDate());
                imgTypeEntity.setStatus("0");
                imgTypeEntity.setType(type);
                imgTypeEntity.setYear(year);
                imgTypeEntity.setPid(pid);
                imgTypeEntity.setImgType(imgType);
                imgTypeMapper.insert(imgTypeEntity);
            }
            return Resut.ok("上传成功");
        } catch (IOException e) {
            log.error(e.toString(), e);
        }


        return Resut.ok("上传失败");

    }


    /**
     * 上传监测点
     * @param multipartFile
     * @return
     */

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
            list = ExcelUtils.readExcelToEntity(DataEntity.class, multipartFile.getInputStream(), name);
//            List<DataEntity> dataEntities = ExcelUtils.readExcelToEntity(DataEntity.class, multipartFile.getInputStream(), name);
            System.out.println(list);
            for (DataEntity dataEntity : list) {
                if(dataEntity.getId()==null){
                    continue;
                }
                System.out.println(dataEntity);
                DataEntity dataEntity1 = dataMapper.selectById(dataEntity.getSamplecode());
                if(dataEntity1==null){
                    dataMapper.insert(dataEntity);
                }else{
                    return Resut.error(202);
                }

            }
            log.info("数据是"+list);
        } catch (IOException e) {
            e.printStackTrace();
            return Resut.error("文件模板错误");
        } catch (Exception e) {
            e.printStackTrace();
            return Resut.error("文件中的id和数据库重复");
        }
//        multipartFile.getInputStream()

        return Resut.ok("上传成功");
    }

    @RequestMapping(value = "/updateData",method = RequestMethod.GET)
    @ResponseBody
    public Resut updateData(){
        for (DataEntity dataEntity : list) {
            System.out.println(dataEntity);
            DataEntity dataEntity1 = dataMapper.selectById(dataEntity.getSamplecode());
            if(dataEntity1==null){
                dataMapper.insert(dataEntity);
            }else{
                dataMapper.updateById(dataEntity1);
            }
        }
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
    public Resut getData(String name, Integer index,String type,String year) throws IllegalAccessException {
        Map<String,Object> mapAll = new HashMap<>();
        List<GradeCode> listName = new ArrayList<>();
        if(StringUtils.isEmpty(year)){
            return Resut.ok(getDataForTotal(type));
        }else{
            Map<String,Object> map = new HashMap<>();
            map.put("type",type);
            map.put("years",year);
            List<DataEntity> all = dataMapper.selectByMap(map);

            List<String> elements = dataMapper.getElement(name,type,year);
            elements.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    String name1 = o1.substring(0,1);
                    String name2 = o2.substring(0,1);
                    //String s =
                    int number1 = 0;
                    int number2 = 0;
                    if(!StringUtils.isEmpty(NumberUtil.toNumber(name1))){
                        number1 = Integer.parseInt(NumberUtil.toNumber(name1));
                    }
                    if(!StringUtils.isEmpty(NumberUtil.toNumber(name2))){
                        number2 = Integer.parseInt(NumberUtil.toNumber(name2));
                    }
                    return number1-number2;
                }
            });
            List<GradeCode> gradeCodes = gradeCodeMapper.selectByMap(null);
            for (DataEntity dataEntity : all) {
                String longitude = dataEntity.getXx();
                String latitude = dataEntity.getYy();
                List<String> list = new ArrayList<>();
                list.add(longitude);
                list.add(latitude);
                Field[] fields = dataEntity.getClass().getDeclaredFields();
                fields[index].setAccessible(true);
                if(fields[index].getName().equals(name)){
                    for (int i = 0; i < elements.size(); i++) {
                        //如果值相等
                        if(elements.get(i).equals(fields[index].get(dataEntity))){
                            dataEntity.setStyle(i);
                            gradeCodes.get(i).setName(elements.get(i));
                            if (listName.indexOf(gradeCodes.get(i))==-1){
                                listName.add(gradeCodes.get(i));
                            }
                        }
                    }
                }
                dataEntity.setLnglat(list);
            }
            Collections.sort(listName);
            mapAll.put("data",all);
            mapAll.put("listName",listName);
            mapAll.put("years",year);

            return Resut.ok(mapAll);
        }

    }


    public Map<String,Object> getDataForTotal(String type){
        Map<String,Object> mapAll = new HashMap<>();
        List<Map<String, Object>> totalTypeForYears = dataMapper.getTotalTypeForYears(type);
        String year;
        if(totalTypeForYears.size()>0&& totalTypeForYears!=null){
            Map<String, Object> map = totalTypeForYears.get(0);
            year = map.get("years").toString();
            List<DataEntity> dataEntities = dataMapper.selectList(
                    new QueryWrapper<DataEntity>()
                    .eq("type", type)
                    .eq("years", map.get("years"))
            );

            for (DataEntity dataEntity : dataEntities) {
                String longitude = dataEntity.getXx();
                String latitude = dataEntity.getYy();
                List<String> list = new ArrayList<>();
                list.add(longitude);
                list.add(latitude);
                dataEntity.setLnglat(list);
//                dataEntity.setStyle(0);
            }
            mapAll.put("data",dataEntities);
            mapAll.put("listName",new ArrayList<>());
            mapAll.put("years",year);
        }

        return mapAll;

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



}
