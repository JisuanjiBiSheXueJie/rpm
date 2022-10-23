package com.rpm.domain;

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
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Msg对象", description="")
public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id id")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "msg_title 标题")
    private String msgTitle;

    @ApiModelProperty(value = "send_name 创建人名称")
    private String sendName;

    @ApiModelProperty(value = "content 消息内容")
    private String content;

    @ApiModelProperty(value = "对应业主id")
    private String tosendId;

    @ApiModelProperty(value = "发送时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date sendTime;

    @ApiModelProperty(value = "is_red 是否阅读（0=未阅读，1=已阅读）")
    private Integer isRed;

    @ApiModelProperty(value = "is_del 是否删除（0=未删除，1=已删除）")
    @TableLogic
    private Integer isDel;

    @ApiModelProperty(value = "用户是否删除（0=未删除，1=已删除）")
    private Integer userDel;

    @ApiModelProperty(value = "create_by 创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "update_by 更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    @ApiModelProperty(value = "消息接收人")
    @TableField(exist = false)
    private String realname;



}
