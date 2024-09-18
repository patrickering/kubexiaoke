package com.xiaoke.common.core.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * JSON 工具
 *
 * @author xiaoke
 */
public class JsonUtils {

    public static Map<String, Object> JSONObjectConvertMap(final String json) {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        final JSONObject js = JSONObject.fromObject(json);
        populate(js, map);
        return map;
    }

    /**
     * 从指定的map对象中获取要设置的值后组装成返回给前台的JSON对象字符串.
     *
     * @param map
     * @param count 总数
     * @return
     */
    public static String fromMap(final Map<String, Object> map, final int count) {
        final Iterator<String> it = map.keySet().iterator();
        final JSONArray options = new JSONArray();
        while (it.hasNext()) {
            final JSONObject option = new JSONObject();
            final String key = (String) it.next();
            option.put(key, map.get(key));
            options.add(option);
        }
        final JSONObject result = new JSONObject();
        result.put("data", options.toString());
        final JSONObject page = new JSONObject();
        page.put("totalRowNum", count);
        result.put("pageInfo", page);
        return result.toString();
    }

    /**
     * 从指定的list[map]对象中获取要设置的值后组装成返回给前台的JSON对象字符串
     *
     * @param list
     * @param count 总数
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String formListMap(final List<Object> list, final Object count) {
        final JSONArray options = new JSONArray();
        for (final Object obj : list) {
            final Iterator<String> it = ((Map<String, Object>) obj).keySet().iterator();
            final JSONObject option = new JSONObject();
            while (it.hasNext()) {
                final String key = (String) it.next();
                Object value = ((Map<String, Object>) obj).get(key);
                value = value != null ? value : "";
                option.put(key, value);
            }
            options.add(option);
        }
        final JSONObject result = new JSONObject();
        result.put("data", options.toString());
        if (count != null) {
            final JSONObject page = new JSONObject();
            page.put("totalRowNum", Integer.parseInt(String.valueOf(count)));
            result.put("pageInfo", page);
        }
        return result.toString();
    }

    @SuppressWarnings("rawtypes")
    public static String formListMap(final List list) {
        final JSONArray options = new JSONArray();
        for (final Object obj : list) {
            final Iterator it = ((Map) obj).keySet().iterator();
            final JSONObject option = new JSONObject();
            while (it.hasNext()) {
                final String key = (String) it.next();
                Object value = ((Map) obj).get(key);
                value = value != null ? value : "";
                option.put(key, value);
            }
            options.add(option);
        }
        final JSONObject result = new JSONObject();
        result.put("data", options.toString());
        return result.toString();
    }

    public static Map<String, Object> jSONObjectConvertMap(final String json) {


        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy/MM/dd HH:mm:ss"));
        //重写json过滤方法
        config.setJsonPropertyFilter(new PropertyFilter() {
            @Override
            public boolean apply(Object obj, String key, Object value) {
                if (value == null||"null".equals(value)||value == "null"||value instanceof JSONNull) {
                    return true;
                }
                return false;
            }
        });
        final HashMap<String, Object> map = new HashMap<String, Object>(1);
        final JSONObject js = JSONObject.fromObject(json,config);
        populate(js, map);
        return map;
    }

    @SuppressWarnings("rawtypes")
    public static List jSONArrayConvertList(final String json) {
        final ArrayList list = new ArrayList();
        final JSONArray ja = JSONArray.fromObject(json);
        populateArray(ja, list);
        return list;
    }

    @SuppressWarnings("rawtypes")
    public static String listToJsonStr(final List list) {
        return JSONArray.fromObject(list).toString();
    }

    @SuppressWarnings("rawtypes")
    public static String mapToJsonStr(final Map map) {
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy/MM/dd HH:mm:ss"));
        //重写json过滤方法
        config.setJsonPropertyFilter(new PropertyFilter() {
            @Override
            public boolean apply(Object obj, String key, Object value) {
                if (value == null) {
                    return true;
                }
                return false;
            }
        });
        return JSONObject.fromObject(map, config).toString();
    }

    @SuppressWarnings({"rawtypes", "unchecked", "unused"})
    public static Map populate(final JSONObject jsonObject, final Map map) {
        for (final Iterator iterator = jsonObject.entrySet().iterator(); iterator
                .hasNext(); ) {
            final String entryStr = String.valueOf(iterator.next());
            final String key = entryStr.substring(0, entryStr.indexOf("="));
            final String value = entryStr.substring(entryStr.indexOf("=") + 1,
                    entryStr.length());
            if (jsonObject.get(key).getClass().equals(JSONObject.class)) {
                final HashMap mapTemp = new LinkedHashMap();
                map.put(key, mapTemp);
                populate(jsonObject.getJSONObject(key), mapTemp);
            } else if (jsonObject.get(key).getClass().equals(JSONArray.class)) {
                final ArrayList list = new ArrayList();
                map.put(key, list);
                populateArray(jsonObject.getJSONArray(key), list);
            } else {
                map.put(key, jsonObject.get(key));
            }
        }
        return map;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void populateArray(final JSONArray jsonArray, final List list) {
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.get(i).getClass().equals(JSONArray.class)) {
                final ArrayList listTemp = new ArrayList();
                list.add(listTemp);
                populateArray(jsonArray.getJSONArray(i), listTemp);
            } else if (jsonArray.get(i).getClass().equals(JSONObject.class)) {
                final HashMap mapTemp = new HashMap(1);
                list.add(mapTemp);
                populate(jsonArray.getJSONObject(i), mapTemp);
            } else {
                list.add(jsonArray.get(i));
            }
        }
    }

    /**
     * Map转换List
     *
     * @param jsonMap
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked", "unused"})
    public static List jsonToArray(final Map jsonMap) {
        final List list = new ArrayList();
        final int size = jsonMap.size();
        for (int i = 0; i < size; i++) {
            final Object o = jsonMap.get(String.valueOf(i));
            list.add(o);
        }
        return list;
    }

    /**
     * List转换Map
     *
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map listToMap(final List jsonList) {
        final Map map = new HashMap(1);
        for (int i = 0; i < jsonList.size(); i++) {
            map.put(String.valueOf(i), jsonList.get(i));
        }
        return map;
    }

    public static Map<String, Object> jsonToMap(final String json) {
        final HashMap<String, Object> map = new HashMap<String, Object>(1);

        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy/MM/dd HH:mm:ss"));
        //重写json过滤方法
        config.setJsonPropertyFilter(new PropertyFilter() {
            @Override
            public boolean apply(Object obj, String key, Object value) {
                if (value == null || value.equals("null")) {
                    return true;
                }
                return false;
            }
        });

        final JSONObject js = JSONObject.fromObject(json,config);
        populate(js, map);
        return map;
    }

    public static Object jsonToObject(String json) {
        final JSONObject js = JSONObject.fromObject(json);
        return js;
    }

    /**
     * 将json转为java类
     *
     * @param str
     * @param clazz
     * @return
     */
    public static <T extends Object> T jsonToObject(String str, Class<?> clazz) {
        //1、使用JSONObject
        if (str != null && !"".equals(str)) {
            JSONObject jsonObject = JSONObject.fromObject(str);
            return (T) JSONObject.toBean(jsonObject, clazz);
        }
        return null;
    }

    /**
     * java对象转json
     *
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        if (obj != null) {
            //将java对象转换为json对象
            JSONObject json = JSONObject.fromObject(obj);
            //将json对象转换为字符串
            String str = json.toString();
            return str;
        }
        return null;
    }

    public static <T> T toSnakeObject(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
// mapper的configure方法可以设置多种配置（例如：多字段 少字段的处理）
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        T reqJson = mapper.readValue(json, clazz);
        return reqJson;
    }


    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String,Object>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            if(value!=null){
                map.put(fieldName, value);
            }
        }
        return map;
    }
}
