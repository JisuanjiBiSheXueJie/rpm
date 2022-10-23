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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author rpm
 * @since 2021-09-15
 */
@Data
@ColumnWidth(30)//注释在具体属性上,设置单独列。注释在类上,统一设置列宽
@HeadRowHeight(30)//设置表头行高
@ContentRowHeight(20)//统一设置数据行行高
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="楼栋对象", description="楼栋对象")
public class Floors implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "楼栋id",index = 0)
    @ApiModelProperty(value = "楼栋id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ExcelProperty(value = "楼栋名称",index = 1)
    @ApiModelProperty(value = "楼栋名称")
    private String floorsName;

    @ExcelProperty(value = "房子总数",index = 2)
    @ApiModelProperty(value = "房子总数")
    private Integer roomsTotal;

    @ExcelProperty(value = "逻辑删除（0=未删除，1=已删除）",index = 3)
    @ApiModelProperty(value = "逻辑删除（0=未删除，1=已删除）")
    private Integer isDel;

    @ExcelProperty(value = "已售总数",index = 4)
    @ApiModelProperty(value = "已售总数")
    private Integer soldTotal;

    @ExcelProperty(value = "楼栋管理员",index = 5)
    @ApiModelProperty(value = "楼栋管理员")
    private String floorsAdmin;

    @ExcelProperty(value = "管理员电话",index = 6)
    @ApiModelProperty(value = "管理员电话")
    private String floorsAdminPhone;

    @ExcelProperty(value = "备注",index = 7)
    @ApiModelProperty(value = "备注")
    private String note;

    @ExcelProperty(value = "创建人",index = 8)
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ExcelProperty(value = "创建时间",index = 9)
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ExcelProperty(value = "更新人",index = 10)
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ExcelProperty(value = "更新时间",index = 11)
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
