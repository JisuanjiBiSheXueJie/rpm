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
import com.rpm.enums.LoginEnum;
import com.rpm.listener.LoginEnumConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 登录日志
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
@ApiModel(value="SysLogLogin对象", description="登录日志")
public class SysLogLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "主键id",index = 0)
    @ApiModelProperty(value = "id")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ExcelProperty(value = "登录名",index = 1)
    @ApiModelProperty(value = "登录名")
    private String loginname;

    @ExcelProperty(value = "登录IP",index = 2)
    @ApiModelProperty(value = "登录ip")
    private String ip;

    @ExcelProperty(value = "操作",index = 3,converter = LoginEnumConverter.class)
    @ApiModelProperty(value = "操作")
    private LoginEnum opt;

    @ExcelProperty(value = "登陆者角色",index = 4)
    @ApiModelProperty(value = "登录者角色")
    private String loginRole;

    @ExcelProperty(value = "登录时间",index = 5)
    @ApiModelProperty(value = "登录时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date optTime;

    @ExcelProperty(value = "登录id",index = 6)
    @ApiModelProperty(value = "登录id")
    private String loginId;

}
