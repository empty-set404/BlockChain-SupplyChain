package com.ivinsight.demo.finance.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

@ApiModel(value = "注册BO")
@Data
public class RegisterBO {
    @ApiModelProperty(value = "用户名")
    String username;
    @ApiModelProperty(value = "组织类型")
    Integer orgType;
    @ApiModelProperty(value = "区块链账户地址")
    String address;
    @ApiModelProperty(value = "账户资金")
    BigInteger credit;

}
