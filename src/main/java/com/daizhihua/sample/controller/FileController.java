package com.daizhihua.sample.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.daizhihua.sample.core.Resut;
import com.daizhihua.sample.dao.*;
import com.daizhihua.sample.entity.*;
import com.daizhihua.sample.util.DateUtil;
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

    @Autowired
    private ImgTypeMapper imgTypeMapper;


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
        /* response.setContentType("image/gif");
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
        }*/

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
        List<GradeCode> listName = new ArrayList<>();
        List<GradeCode> gradeCodes = gradeCodeMapper.selectByMap(null);
        for (DataEntity dataEntity : all) {
            log.info("实体类是"+dataEntity);
            String longitude = dataEntity.getXx();
            String latitude = dataEntity.getYy();
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



}
