package com.ivinsight.demo.finance.controller;

import com.ivinsight.demo.finance.model.bo.ReceiptHistoryBO;
import com.ivinsight.demo.finance.model.bo.RegisterBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.ivinsight.demo.finance.model.Result;
import com.ivinsight.demo.finance.service.IQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@Api(value = "查询相关接口", tags = "查询相关接口")
@RestController
@RequestMapping("/finance/query")
public class QueryController {
    @Autowired
    IQueryService queryService;


    @ApiOperation(value = "创建凭证", notes = "创建凭证")
    @RequestMapping(value = "createReceipt", method = RequestMethod.GET)
    public Result createReceipt(@RequestParam("address") String address){
        String obj = queryService.createReceipt(address);
        return Result.success(obj);
    }

    @RequestMapping(value = "getAllReceipt", method = RequestMethod.GET)
    public Result getAllReceipt(@RequestParam("address") String address){
        return queryService.getAllReceipt(address);
    }

    @RequestMapping(value = "getAllReceiptHistoryIds", method = RequestMethod.GET)
    public Result getAllReceiptHistoryIds(@RequestParam("address") String address,@RequestParam("rid") Integer rid){
        return queryService.getAllReceiptHistoryIds(address,rid);
    }

    @RequestMapping(value = "transReceipt", method = RequestMethod.POST)
    public Result<String> transReceipt(@RequestBody ReceiptHistoryBO receiptHistoryBO) {
        return queryService.transReceipt(receiptHistoryBO);
    }

}
