package com.ivinsight.demo.finance.service;

import com.ivinsight.demo.finance.model.Result;
import com.ivinsight.demo.finance.model.bo.ReceiptHistoryBO;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigInteger;
import java.util.HashMap;

public interface IQueryService {

    String createReceipt(String userAddress);
    Result getAllReceipt(String userAddress);
    HashMap getReceiptDetail(String userAddress, BigInteger rid);
    Result getAllReceiptHistoryIds(String userAddress, Integer rid);
    HashMap getReceiptHistoryDetail(String userAddress, BigInteger rhid);
    Result transReceipt(@RequestBody ReceiptHistoryBO bo);
}
