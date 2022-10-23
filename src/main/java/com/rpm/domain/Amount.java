package com.rpm.domain;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rpm.enums.AmTypeEnum;
import com.rpm.enums.CostTypeEnum;
import com.rpm.enums.PayTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author rpm
 * @since 2021-09-17
 */
@Data
@ColumnWidth(30)//注释在具体属性上,设置单独列。注释在类上,统一设置列宽
@HeadRowHeight(30)//设置表头行高
@ContentRowHeight(20)//统一设置数据行行高
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Amount对象", description="")
public class Amount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id id")
    @ExcelProperty(value = "id",index = 0)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ExcelProperty(value = "收款类型",index = 1)
    @ApiModelProperty(value = "收款类型")
    private AmTypeEnum amountType;

    @ExcelProperty(value = "收款时间",index = 2)
    @ApiModelProperty(value = "amount_time 收款时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date amountTime;

    @ExcelProperty(value = "收款人名称",index = 3)
    @ApiModelProperty(value = "collection_name 收款人名称")
    private String collectionName;

    @ExcelProperty(value = "付款金额",index = 4)
    @ApiModelProperty(value = "pay_amount 付款金额")
    private BigDecimal payAmount;

    @ExcelProperty(value = "付款形式",index = 5)
    @ApiModelProperty(value = "pay_type 付款形式（0=现金，1=微信，2=支付宝）")
    private PayTypeEnum payType;

    @ExcelProperty(value = "付款人对应id",index = 6)
    @ApiModelProperty(value = "付款人对应id")
    private String payId;

    @ExcelProperty(value = "款项事由",index = 7)
    @ApiModelProperty(value = "collection_reason 款项事由")
    private String collectionReason;

    @ExcelProperty(value = "备注",index = 8)
    @ApiModelProperty(value = "note 备注")
    private String note;

    @ExcelProperty(value = "逻辑删除")
    @ApiModelProperty(value = "逻辑删除")
    private Integer isDel;

    @ExcelProperty(value = "创建人",index = 9)
    @ApiModelProperty(value = "create_by 创建人")
    private String createBy;

    @ExcelProperty(value = "更新时间",index = 10)
    @ApiModelProperty(value = "update_time 更新时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ExcelProperty(value = "更新人",index = 11)
    @ApiModelProperty(value = "update_by 更新人")
    private String updateBy;

    @ExcelProperty(value = "更新时间",index = 12)
    @ApiModelProperty(value = "update_time 更新时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "付款人名称")
    @TableField(exist = false)
    private String payName;

    @ApiModelProperty(value = "付款人名称(查询使用)")
    @TableField(exist = false)
    private String realname;

    @ApiModelProperty(value = "对应付款类型")
    @TableField(exist = false)
    private CostTypeEnum costType;

}
