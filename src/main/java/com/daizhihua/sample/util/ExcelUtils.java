package com.daizhihua.sample.util;


import com.daizhihua.sample.entity.ExcelHead;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtils {
    private static final String FULL_DATA_FORMAT = "yyyy/MM/dd  HH:mm:ss";
    private static final String SHORT_DATA_FORMAT = "yyyy/MM/dd";


    /**
     * Excel表头对应Entity属性 解析封装javabean
     *
     * @param classzz    类
     * @param in         excel流
     * @param fileName   文件名
     * @param excelHeads excel表头与entity属性对应关系
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> List<T> readExcelToEntity(Class<T> classzz, InputStream in, String fileName, List<ExcelHead> excelHeads) throws Exception {
        checkFile(fileName);    //是否EXCEL文件
        Workbook workbook = getWorkBoot(in, fileName); //兼容新老版本
        List<T> excelForBeans = readExcel(classzz, workbook, excelHeads);  //解析Excel
        return excelForBeans;
    }

    /**
     * 解析Excel转换为Entity
     *
     * @param classzz  类
     * @param in       excel流
     * @param fileName 文件名
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcelToEntity(Class<T> classzz, InputStream in, String fileName) throws Exception {
        return readExcelToEntity(classzz, in, fileName,null);
    }

    /**
     * 校验是否是Excel文件
     *
     * @param fileName
     * @throws Exception
     */
    public static void checkFile(String fileName) throws Exception {
        if (!StringUtils.isEmpty(fileName) && !(fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {
            throw new Exception("不是Excel文件！");
        }
    }

    /**
     * 兼容新老版Excel
     *
     * @param in
     * @param fileName
     * @return
     * @throws IOException
     */
    private static Workbook getWorkBoot(InputStream in, String fileName) throws IOException {
        if (fileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(in);
        } else {
            return new HSSFWorkbook(in);
        }
    }

    /**
     * 解析Excel
     *
     * @param classzz    类
     * @param workbook   工作簿对象
     * @param excelHeads excel与entity对应关系实体
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> List<T> readExcel(Class<T> classzz, Workbook workbook, List<ExcelHead> excelHeads) throws Exception {
        List<T> beans = new ArrayList<T>();
        int sheetNum = workbook.getNumberOfSheets();
        for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            String sheetName=sheet.getSheetName();
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            Row head = sheet.getRow(firstRowNum);
            if (head == null)
                continue;
            short firstCellNum = head.getFirstCellNum();
            short lastCellNum = head.getLastCellNum();
            Field[] fields = classzz.getDeclaredFields();
            System.out.println(fields.length);
            for (int rowIndex = firstRowNum + 1; rowIndex <= lastRowNum; rowIndex++) {
                Row dataRow = sheet.getRow(rowIndex);
                if (dataRow == null)
                    continue;
                T instance = classzz.newInstance();
                if(CollectionUtils.isEmpty(excelHeads)){  //非头部映射方式，默认不校验是否为空，提高效率
                    firstCellNum=dataRow.getFirstCellNum();
                    lastCellNum=dataRow.getLastCellNum();
                }
                for (int cellIndex = firstCellNum; cellIndex < lastCellNum; cellIndex++) {
                    Cell headCell = head.getCell(cellIndex);
                    if (headCell == null)
                        continue;
                    Cell cell = dataRow.getCell(cellIndex);
                   // headCell.setCellType(Cell.CELL_TYPE_STRING);
                    String headName = headCell.getStringCellValue().trim();
                    if (StringUtils.isEmpty(headName)) {
                        continue;
                    }
//                    ExcelHead eHead = null;
//                    if (!CollectionUtils.isEmpty(excelHeads)) {
//                        for (ExcelHead excelHead : excelHeads) {
//                            if (headName.toLowerCase().equals(excelHead.getExcelName().toLowerCase())) {
//                                eHead = excelHead;
//                                headName = eHead.getEntityName();
//                                break;
//                            }
//                        }
//                    }
                    Field field = fields[cellIndex];
                    String methodName = MethodUtils.setMethodName(field.getName());
                    Method method = classzz.getMethod(methodName, field.getType());
                    if (isDateFied(field)) {
                        Date date=null;
                        if(cell!=null){
                            date=cell.getDateCellValue();
                        }
//                        if (date == null) {
//                            volidateValueRequired(eHead,sheetName,rowIndex);
//                            break;
//                        }
                        method.invoke(instance, cell.getDateCellValue());
                    } else {
                        String value = null;
                        if(cell!=null){
                            //cell.setCellType(Cell.CELL_TYPE_STRING);
                            value=getCellValue(cell);
                        }
                        if (StringUtils.isEmpty(value)) {
                            //volidateValueRequired(eHead,sheetName,rowIndex);
                            //break;
                            value = "";
                        }
                        method.invoke(instance, convertType(field.getType(), value.trim()));
                    }


                }
                beans.add(instance);
            }
        }
        return beans;
    }

    private static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //判断数据的类型
        switch (cell.getCellType()){
            case NUMERIC: //数字
                cellValue = stringDateProcess(cell);
                DataFormatter dataFormatter = new DataFormatter();
                cellValue = dataFormatter.formatCellValue(cell);
                break;
            case STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: //空值
                cellValue = "";
                break;
            case ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public static String stringDateProcess(Cell cell){
        String result;
        DataFormatter dataFormatter = new DataFormatter();
//        System.out.println("判断"+ HSSFDateUtil.isCellDateFormatted(cell));
        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
            SimpleDateFormat sdf = null;
//            System.out.println(cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm"));
            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("hh:mm")) {
                sdf = new SimpleDateFormat("HH:mm");
            } else {// 日期
                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            }
            Date date = cell.getDateCellValue();
            result = sdf.format(date);
//            result = dataFormatter.formatCellValue(cell);;
        } else if (cell.getCellStyle().getDataFormat() == 14|| cell.getCellStyle().getDataFormat() == 31||
                cell.getCellStyle().getDataFormat()==57 ||cell.getCellStyle().getDataFormat()==58) {
            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            double value = cell.getNumericCellValue();
            Date date = org.apache.poi.ss.usermodel.DateUtil
                    .getJavaDate(value);
            result = sdf.format(date);
        } else {


            result = dataFormatter.formatCellValue(cell);
           /* double value = cell.getNumericCellValue();
            CellStyle style = cell.getCellStyle();
            DecimalFormat format = new DecimalFormat();
            String temp = style.getDataFormatString();
            // 单元格设置成常规
            if (temp.equals("General")) {
                format.applyPattern("#");
            }
            result = format.format(value);*/
        }

        return result;
    }

    /**
     * 是否日期字段
     *
     * @param field
     * @return
     */
    private static boolean isDateFied(Field field) {
        return (Date.class == field.getType());
    }
    /**
     * 空值校验
     *
     * @param excelHead
     * @throws Exception
     */
    private static void volidateValueRequired(ExcelHead excelHead,String sheetName,int rowIndex) throws Exception {
        if (excelHead != null && excelHead.isRequired()) {
            throw new Exception("《"+sheetName+"》第"+(rowIndex+1)+"行:\""+excelHead.getExcelName() + "\"不能为空！");
        }
    }
    /**
     * 类型转换
     *
     * @param classzz
     * @param value
     * @return
     */
    private static Object convertType(Class classzz, String value) {
        if (Integer.class == classzz || int.class == classzz) {
            return Integer.valueOf(value);
        }
        if (Short.class == classzz || short.class == classzz) {
            return Short.valueOf(value);
        }
        if (Byte.class == classzz || byte.class == classzz) {
            return Byte.valueOf(value);
        }
        if (Character.class == classzz || char.class == classzz) {
            return value.charAt(0);
        }
        if (Long.class == classzz || long.class == classzz) {
            return Long.valueOf(value);
        }
        if (Float.class == classzz || float.class == classzz) {
            return Float.valueOf(value);
        }
        if (Double.class == classzz || double.class == classzz) {
            return Double.valueOf(value);
        }
        if (Boolean.class == classzz || boolean.class == classzz) {
            return Boolean.valueOf(value.toLowerCase());
        }
        if (BigDecimal.class == classzz) {
            return new BigDecimal(value);
        }
       /* if (Date.class == classzz) {
            SimpleDateFormat formatter = new SimpleDateFormat(FULL_DATA_FORMAT);
            ParsePosition pos = new ParsePosition(0);
            Date date = formatter.parse(value, pos);
            return date;
        }*/
        return value;
    }
    /**
     * 获取properties的set和get方法
     */
    static class MethodUtils {
        private static final String SET_PREFIX = "set";
        private static final String GET_PREFIX = "get";
        private static String capitalize(String name) {
            if (name == null || name.length() == 0) {
                return name;
            }
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        public static String setMethodName(String propertyName) {
            return SET_PREFIX + capitalize(propertyName);
        }
        public static String getMethodName(String propertyName) {
            return GET_PREFIX + capitalize(propertyName);
        }
    }
}