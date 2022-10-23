package com.rpm.domain;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.annotations.Select;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author rpm
 * @since 2021-09-01
 */
@Data
@ColumnWidth(30)//注释在具体属性上,设置单独列。注释在类上,统一设置列宽
@HeadRowHeight(30)//设置表头行高
@ContentRowHeight(20)//统一设置数据行行高
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "主键id",index = 0)
    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ExcelProperty(value = "真实名字",index = 1)
    @ApiModelProperty(value = "真实名字")
    private String realname;

    @ExcelProperty(value = "用户名",index = 2)
    @ApiModelProperty(value = "用户名")
    private String username;

    @ExcelProperty(value = "密码（加密后）",index = 3)
    @ApiModelProperty(value = "密码（加密后）")
    private String password;

    @ExcelProperty(value = "电话",index = 4)
    @ApiModelProperty(value = "电话")
    private String phone;

    @ExcelProperty(value = "邮箱",index = 5)
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ExcelProperty(value = "楼栋id",index = 6)
    @ApiModelProperty(value = "楼栋id")
    private String foolrsId;

    @ExcelProperty(value = "角色",index = 7)
    @ApiModelProperty(value = "角色")
    private String role;

    @ExcelProperty(value = "盐",index = 8)
    @ApiModelProperty(value = "盐")
    @TableField(fill = FieldFill.INSERT)
    private String salt;

    @ExcelProperty(value = "主键状态（0=正常，1=冻结",index = 9)
    @ApiModelProperty(value = "状态（0=正常，1=冻结）")
    private Integer status;

    @ExcelProperty(value = "头像",index = 10)
    @ApiModelProperty(value = "头像")
    private String avatar;

    @ExcelProperty(value = "生日",index = 11)
    @ApiModelProperty(value = "生日")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    @ExcelProperty(value = "性别（0=女，1=男）",index = 12)
    @ApiModelProperty(value = "性别（0=女，1=男）")
    private Integer sex;

    @ExcelProperty(value = "逻辑删除（0=未删除，1=已删除）",index = 13)
    @ApiModelProperty(value = "逻辑删除（0=未删除，1=已删除）")
    @TableLogic
    private Integer isDel;

    @ExcelProperty(value = "详细地址",index = 14)
    @ApiModelProperty(value = "详细地址")
    private String address;

    @ExcelProperty(value = "备注",index = 15)
    @ApiModelProperty(value = "备注")
    private String note;

    @ExcelProperty(value = "创建人",index = 16)
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ExcelProperty(value = "创建时间",index = 17)
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ExcelProperty(value = "更新人",index = 18)
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ExcelProperty(value = "更新时间",index = 19)
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ExcelProperty(value = "删除人",index = 20)
    @ApiModelProperty(value = "删除人")
    private String delBy;

    @ExcelProperty(value = "删除时间",index = 21)
    @ApiModelProperty(value = "删除时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date delTime;

    @ExcelProperty(value = "冻结人",index = 22)
    @ApiModelProperty(value = "冻结人")
    private String freezeBy;

    @ExcelProperty(value = "冻结时间",index = 23)
    @ApiModelProperty(value = "冻结时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date freezeTime;

}
