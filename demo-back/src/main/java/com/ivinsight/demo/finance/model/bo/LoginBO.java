package com.ivinsight.demo.finance.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户登录 BO")
public class LoginBO {
    @ApiModelProperty(value = "用户地址")
    String address;
    @ApiModelProperty(value = "用户名")
    String userName;

}
