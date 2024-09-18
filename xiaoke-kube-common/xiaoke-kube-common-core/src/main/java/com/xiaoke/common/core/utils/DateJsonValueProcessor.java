package com.xiaoke.common.core.utils;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*** 
 * 将Bean中的Timestamp转换为json中的日期字符串 
 * @author xiaoke
 * */  
public class DateJsonValueProcessor implements JsonValueProcessor {
    public static final String DEFAULT_DATE_PATTERN_TIME ="yyyy-MM-dd";
    private DateFormat dateFormat ;
    public DateJsonValueProcessor(String datePattern){
        try{
            dateFormat  = new SimpleDateFormat(datePattern);}
        catch(Exception e ){
            dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN_TIME);
        }
    }
    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        if(null!=value){  
            return process(value);  
        }else{  
            return "";  
        }  
    }
    @Override
    public Object processObjectValue(String key, Object value,JsonConfig jsonConfig) {
        if(null!=value){
            return process(value);
        }else{
            return "";
        }
    }
    private Object process(Object value){
        return dateFormat.format((Date)value);
    }  
}  
