package com.rpm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value ="修改权限Vo",description = "修改权限Vo")
public class ChangeRoleVo {
    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户姓名")
    private String realname;

    @ApiModelProperty(value = "权限")
    private String role;

    @ApiModelProperty(value = "权限code")
    private Integer currRole;
}
