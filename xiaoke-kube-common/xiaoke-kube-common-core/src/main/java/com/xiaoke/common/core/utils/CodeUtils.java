package com.xiaoke.common.core.utils;

/**
 * 邀请码生成
 * @author 邢敬越
 * @date 2020/12/29
 */
public class CodeUtils {

    public static String code(){
        String randomcode = "";
        // 用字符数组的方式随机
        String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] m = model.toCharArray();
        for (int j = 0; j < 6; j++) {
            char c = m[(int) (Math.random() * 36)];
            // 保证六位随机数之间没有重复的
            if (randomcode.contains(String.valueOf(c))) {
                j--;
                continue;
            }
            randomcode = randomcode + c;
        }
       return randomcode;
    }
}
