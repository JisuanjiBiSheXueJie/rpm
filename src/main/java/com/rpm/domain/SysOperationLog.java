package com.rpm.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rpm.enums.LevelEnum;
import com.rpm.enums.OptTypeEnum;
import com.rpm.listener.LevelEnumConverter;
import com.rpm.listener.OptEnumConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author rpm
 * @since 2021-09-11
 */
@Data
@ColumnWidth(30)//注释在具体属性上,设置单独列。注释在类上,统一设置列宽
@HeadRowHeight(30)//设置表头行高
@ContentRowHeight(20)//统一设置数据行行高
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysOperationLog对象", description="操作日志")
public class SysOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "主键id",index = 0)
    @ApiModelProperty(value = "id")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ExcelProperty(value = "操作人",index = 1)
    @ApiModelProperty(value = "操作人")
    private String optname;

    @ExcelProperty(value = "执行的方法",index = 2)
    @ApiModelProperty(value = "执行的方法")
    private String methods;

    @ExcelProperty(value = "操作人角色",index = 3)
    @ApiModelProperty(value = "操作人角色")
    private String role;

    @ExcelProperty(value = "操作类型",index = 4 ,converter = OptEnumConverter.class)
    @ApiModelProperty(value = "操作类型")
    private OptTypeEnum optType;

    @ExcelProperty(value = "操作描述",index = 5)
    @ApiModelProperty(value = "描述")
    private String detail;

    @ExcelProperty(value = "操作等级",index = 6, converter = LevelEnumConverter.class)
    @ApiModelProperty(value = "等级")
    private LevelEnum logLevel;

    @ExcelProperty(value = "操作ip",index = 7)
    @ApiModelProperty(value = "操作ip")
    private String ip;

    @ExcelProperty(value = "操作时间",index = 8)
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date optTime;


}
