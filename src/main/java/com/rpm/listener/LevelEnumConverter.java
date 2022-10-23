package com.rpm.listener;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.rpm.enums.LevelEnum;
import org.springframework.stereotype.Component;

/**
 * 操作等级枚举转换器
 */
@Component
public class LevelEnumConverter implements Converter<LevelEnum> {
    @Override
    public Class<LevelEnum> supportJavaTypeKey() {
        return LevelEnum.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LevelEnum convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(LevelEnum levelEnum, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new CellData<>(levelEnum.getName());
    }
}
