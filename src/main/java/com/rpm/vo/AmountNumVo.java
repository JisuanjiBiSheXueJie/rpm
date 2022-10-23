package com.rpm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel(value ="最近七天缴费情况Vo",description = "最近七天缴费情况Vo")
public class AmountNumVo {

    @ApiModelProperty(value = "缴费日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date amountTime;

    @ApiModelProperty(value = "缴费总金额")
    private Integer amountCount;
}
