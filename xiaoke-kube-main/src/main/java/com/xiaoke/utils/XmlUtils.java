package com.xiaoke.utils;

/**
 * xml工具
 */
public class XmlUtils {
    /**
     * 字符串转义
     * @param str
     * @return
     */
    public static String paraphrase(String str){
        str = str.replaceAll("&","&amp;");
        str = str.replaceAll("<","&lt;");
        str = str.replaceAll(">","&gt;");
        str = str.replaceAll("\"","&quot;");
        str = str.replaceAll("©","&copy;");
        str = str.replaceAll("®","&reg");
        str = str.replaceAll("'","&apos;");
        return str;
    }
}
