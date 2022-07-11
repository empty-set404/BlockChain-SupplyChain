package com.ivinsight.demo.finance.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ivinsight.demo.finance.model.Result;
import com.ivinsight.demo.finance.model.bo.ReceiptHistoryBO;
import com.ivinsight.demo.finance.model.vo.*;
import com.ivinsight.demo.finance.utils.IOUtil;
import com.ivinsight.demo.finance.service.IQueryService;
import com.ivinsight.demo.finance.utils.WeBASEUtils;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class QueryServiceImpl implements IQueryService {
    @Autowired
    WeBASEUtils weBASEUtils;


    public static final String ABI = IOUtil.readResourceAsString("abi/MySupplyChain.abi");

    /**
     * @description:创建凭证
     * @param {String} userAddress
     * @return {*}
     */
    public String createReceipt(String userAddress) {

        List funcParam = new ArrayList();
        funcParam.add(userAddress);

        String _result = weBASEUtils.funcPost(userAddress,"createReceipt",funcParam);
        JSONObject obj = JSONUtil.parseObj(_result);

        return obj.getStr("transactionHash");
    }

    /**
     * @description: 获取所有凭证
     * @param {String} userAddress
     * @return {*}
     */
    public Result getAllReceipt(String userAddress) {
        if (StringUtil.isNullOrEmpty(userAddress)) {
            return Result.error(ResultVO.PARAM_EMPTY);
        }
        // 返回数组字符串
        String getAllReceipt = weBASEUtils.funcPost(userAddress, "getAllReceipt", Arrays.asList());
        System.out.println("getAllReceipt================[\"[ 1, 2, 3, 4 ]\"]"+getAllReceipt);

        JSONArray data = JSONUtil.parseArray(JSONUtil.parseArray(getAllReceipt).get(0));

        ArrayList<Object> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            HashMap receiptDetail = this.getReceiptDetail(userAddress, data.getBigInteger(i));
            result.add(receiptDetail);
        }

        return Result.success(result);
    }

    /**
     * @description: 获取凭证详情
     * @param {String} userAddress
     * @param {BigInteger} rid
     * @return {*}
     */
    public HashMap getReceiptDetail(String userAddress, BigInteger rid) {

        List funcParam = new ArrayList();
        funcParam.add(rid);

        String _result = weBASEUtils.funcPost(userAddress,"getReceiptDetails",funcParam);
        JSONArray objArray = JSONUtil.parseArray(_result);
        HashMap _resultMap = new HashMap();
        _resultMap.put("id",objArray.getBigInteger(0));
        _resultMap.put("address",objArray.getStr(1));
        return _resultMap;
    }


    /**
     * @description: 根据凭证id获取所有交易历史信息
     * @param {String} userAddress
     * @param {Integer} rid
     * @return {*}
     */
    public Result getAllReceiptHistoryIds(String userAddress, Integer rid) {
        List funcParam = new ArrayList();
        funcParam.add(rid);

        String _result = weBASEUtils.funcPost(userAddress,"getAllReceiptHostoryIds",funcParam);
        JSONArray objects = JSONUtil.parseArray(JSONUtil.parseArray(_result).get(0));

        List receiptList = new ArrayList();

        for (int i = 0; i < objects.size(); i++) {
            BigInteger rhid = objects.getBigInteger(i);
            HashMap detail = getReceiptHistoryDetail(userAddress,rhid);
            receiptList.add(detail);
        }
        return Result.success(receiptList);
    }

    /**
     * @description: 获取凭证交易历史详情
     * @param {String} userAddress
     * @param {BigInteger} rhid
     * @return {*}
     */
    public HashMap getReceiptHistoryDetail(String userAddress, BigInteger rhid) {

        List funcParam = new ArrayList();
        funcParam.add(rhid);

        String _result = weBASEUtils.funcPost(userAddress,"getReceiptHistoryDetail",funcParam);
        JSONArray objArray = JSONUtil.parseArray(_result);
        HashMap _resultMap = new HashMap();
        _resultMap.put("rHid",objArray.getBigInteger(0));
        _resultMap.put("rId",objArray.getBigInteger(1));
        _resultMap.put("senderAddress",objArray.getStr(2));
        _resultMap.put("receiverAddress",objArray.getStr(3));
        _resultMap.put("amount",objArray.getBigInteger(4));
        _resultMap.put("createTime",objArray.getBigInteger(5));

        return _resultMap;
    }

    /**
     * @description: 交易凭证
     * @param {*}
     * @return {*}
     */
    public Result transReceipt(@RequestBody ReceiptHistoryBO bo) {

        List funcParam = new ArrayList();
        funcParam.add(bo.getRId());
        funcParam.add(bo.getSenderAddress());
        funcParam.add(bo.getReceiverAddress());
        funcParam.add(bo.getAmount());

        String _result = weBASEUtils.funcPost(bo.getReceiverAddress(),"transReceipt",funcParam);
        JSONObject _resultJson = JSONUtil.parseObj(_result);
        if (_resultJson.getBool("statusOK") != true){ // _resultJson.getInt("code") > 0
            if (_resultJson.getInt("message") == 500002){
                return Result.error(ResultVO.QUERY_EXISTS);
            }
            if (_resultJson.getInt("message") == 400001){
                return Result.error(ResultVO.PARAM_EMPTY);
            }
            if (_resultJson.getInt("message") == 400002){
                return Result.error(ResultVO.QUERY_EMPTY);
            }
            if (_resultJson.getInt("message") == 500001){
                return Result.error(ResultVO.AMOUNT_NOT_ENOUGH);
            }
            return Result.error(ResultVO.CONTRACT_ERROR);
        }

        return Result.success("ok");
    }

}
