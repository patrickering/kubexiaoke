package com.xiaoke.common.core.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyExcelUtils<T> {
    /**
     * 导出模板
     */
    public static <T> Boolean  downloadTemplate(HttpServletResponse response, Class<T> clazz, List<DropDown> dropDownList,String fileName) {
        try {
            response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName+"导入模板.xlsx","UTF8"));
            response.setContentType("application/octet-stream; charset=UTF-8");
            // 响应字段对应的下拉集合
            Map<Integer, String[]> map = new HashMap<>();
            for (DropDown dropDown : dropDownList) {
                map.put(dropDown.getIndex(), dropDown.getData());
            }
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(new ModelCellWriteHandler(map)).build();
            WriteSheet sheet = EasyExcel.writerSheet(0, fileName).build();
            excelWriter.write(new ArrayList<>(), sheet);
            excelWriter.finish();
            return Boolean.TRUE;
        }catch (Exception e){
            return Boolean.FALSE;
        }
    }

    /**
     * 同步按模型读（默认读取sheet0,从第2行开始读）
     *
     * @param inputStream 输入流
     * @param clazz       模型的类类型（excel数据会按该类型转换成对象）
     * @return
     * @demo 字段默认会按照字段顺序
     * 可以标注模板字段注解 @ExcelProperty
     * 直接使用每列第一行名称 如@ExcelProperty(name = "法人身份证号") 默认name可以不写
     * 或者使用列数 @ExcelProperty("index = 0")
     * 不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配
     */
    public static <T> List<T> syncReadModel(InputStream inputStream, Class<T> clazz) {
        return EasyExcel.read(inputStream).head(clazz).sheet().doReadSync();
    }


    /**
     * 根据excel模板文件写入文件
     *
     * @param response 设置输出流
     * @param clazz    模型的类 类型（excel数据会按该类型转换成对象）
     * @param data     数据
     *                 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     */
    public static void writeOutputStreamExcel(HttpServletResponse response, Class clazz, List data) throws IOException {
        // 这里注意 使用swagger会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        //文件名由前端生成，也可以自己设置
        EasyExcel.write(response.getOutputStream(), clazz).sheet("sheet1").registerWriteHandler(new ExcelWidthStyleStrategy()).doWrite(data);
    }
}
