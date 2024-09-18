package com.xiaoke.common.core.utils.excel;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.Map;

public class ModelCellWriteHandler implements SheetWriteHandler {
 
    private Map<Integer,String[]> map = null;
 
    public ModelCellWriteHandler(Map<Integer,String[]> map){
        this.map = map;
    }
 
    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }
 
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 这里可以对cell进行任何操作
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // k 为存在下拉数据集的单元格下表 v为下拉数据集
        map.forEach((k, v) -> {
            // 下拉列表约束数据
            DataValidationConstraint constraint = helper.createExplicitListConstraint(v);
            // 设置下拉单元格的首行 末行 首列 末列
            CellRangeAddressList rangeList = new CellRangeAddressList(1, 65536, k, k);
            // 设置约束
            DataValidation validation = helper.createValidation(constraint, rangeList);
            // 阻止输入非下拉选项的值
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.setShowErrorBox(true);
            validation.setSuppressDropDownArrow(true);
            validation.createErrorBox("提示","此值与单元格定义格式不一致");
            // validation.createPromptBox("填写说明：","填写内容只能为下拉数据集中的单位，其他单位将会导致无法入仓");
            sheet.addValidationData(validation);
        });
    }
}