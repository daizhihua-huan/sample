package com.daizhihua.sample.core;

import java.io.Serializable;

/**
 * 后台返回前端的统一封装
 * @ClassName Result 封装返回的结果
 * @versio代志华
 */
public class Resut implements Serializable {

    public static final int SUCESS = 200;

    public static final int ERROR = 201;

    //结果集
    private int status ;

    public int getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    //信息
    private Object data;


    /**
     * 通过此方法获取结果集
     * @param
     * @Param status 状态码
     * @return
     */
    private static Resut resut(Object data,int status){
        Resut resut = new Resut();
        resut.setData(data);
        resut.setStatus(status);
        return resut;
    }



    //请求成功不需要返回数据调用
    public static Resut ok(){
        return resut("success",SUCESS);
    }

    //请求失败 不返回数据调用
    public static Resut error(){
        return resut("error",ERROR);
    }

    //请求失败 不返回数据调用
    public static Resut error(int status){
        return resut("error",status);
    }

    //数据请求成功且有数据调用
    public static Resut ok(Object data){
        return resut(data,SUCESS);
    }

    //数据返回失败且有数据调用
    public static Resut error(Object data){
        return resut(data,ERROR);
    }





}
