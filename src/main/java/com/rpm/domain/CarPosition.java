package com.rpm.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author rpm
 * @since 2021-09-17
 */
@Data
@ColumnWidth(30)//注释在具体属性上,设置单独列。注释在类上,统一设置列宽
@HeadRowHeight(30)//设置表头行高
@ContentRowHeight(20)//统一设置数据行行高
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="车位统计对象", description="车位统计对象")
public class CarPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "车位id",index = 0)
    @ApiModelProperty(value = "车位id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ExcelProperty(value = "车位编号",index = 1)
    @ApiModelProperty(value = "车位编号")
    private String carNum;

    @ExcelProperty(value = "车牌号",index = 2)
    @ApiModelProperty(value = "车牌号")
    private String carLicense;

    @ExcelProperty(value = "车位面积",index = 3)
    @ApiModelProperty(value = "车位面积")
    private BigDecimal area;

    @ExcelProperty(value = "对应楼栋d",index = 4)
    @ApiModelProperty(value = "对应楼栋d")
    private String floorId;

    @ExcelProperty(value = "车位照片",index = 5)
    @ApiModelProperty(value = "车位照片")
    private String imgUrl;

    @ExcelProperty(value = "对应业主id",index = 6)
    @ApiModelProperty(value = "对应业主id")
    private String ownerId;

    @ExcelProperty(value = "启用时间",index = 7)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "启用时间")
    private Date startTime;

    @ExcelProperty(value = "停用时间",index = 8)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "停用时间")
    private Date endTime;

    @ExcelProperty(value = "费用",index = 9)
    @ApiModelProperty(value = "费用")
    private BigDecimal amount;

    @ExcelProperty(value = "状态（0=使用中，1=未使用，2=损坏）",index = 10)
    @ApiModelProperty(value = "状态（0=使用中，1=未使用，2=损坏）")
    private Integer status;

    @ExcelProperty(value = "逻辑删除",index = 11)
    @ApiModelProperty(value = "逻辑删除")
    private Integer isDel;

    @ExcelProperty(value = "管理员",index = 12)
    @ApiModelProperty(value = "管理员")
    private String manager;

    @ExcelProperty(value = "管理员电话",index = 13)
    @ApiModelProperty(value = "管理员电话")
    private String managerPhone;

    @ExcelProperty(value = "备注",index = 14)
    @ApiModelProperty(value = "备注")
    private String note;

    @ExcelProperty(value = "创建人",index = 15)
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ExcelProperty(value = "创建时间",index = 16)
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ExcelProperty(value = "更新人",index = 17)
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ExcelProperty(value = "更新时间",index = 18)
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
