package com.rpm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value ="修改密码Vo",description = "修改密码Vo")
public class ChangePassWordVo {
    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "新的密码")
    private String password;

    @ApiModelProperty(value = "确认密码")
    private String changepa;

}
