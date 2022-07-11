package com.ivinsight.demo.finance.model.bo;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ReceiptHistoryBO {
    BigInteger rhId;
    BigInteger rId;
    String senderAddress;
    String receiverAddress;
    BigInteger amount;
}
