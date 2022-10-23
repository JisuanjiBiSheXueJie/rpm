package com.rpm.listener;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.rpm.enums.OptTypeEnum;
import org.springframework.stereotype.Component;

/**
 * 操作类型枚举转换器
 */
@Component
public class OptEnumConverter implements Converter<OptTypeEnum> {

    @Override
    public Class<OptTypeEnum> supportJavaTypeKey() {
        return OptTypeEnum.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public OptTypeEnum convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    /**
     * 获取枚举的字段
     * @param optTypeEnum
     * @param excelContentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public CellData convertToExcelData(OptTypeEnum optTypeEnum, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new CellData<>(optTypeEnum.getName());
    }
}
