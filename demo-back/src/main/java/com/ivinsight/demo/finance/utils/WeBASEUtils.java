/*
 * @description:
 * @param:
 * @return:
 */
package com.ivinsight.demo.finance.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.netty.util.internal.StringUtil;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.fisco.bcos.sdk.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;
import java.util.List;

@Service
public class WeBASEUtils {

    @Value("${project.webase-url}")
    String webaseUrl;

    @Value("${system.contract.CreditLetterAddress}")
    String creditLetterAddress;

    public static final String ABI = IOUtil.readResourceAsString("abi/MySupplyChain.abi");

    /**
     * 发送请求调用链接口
     * @param userAddress
     * @param funcName
     * @param funcParam
     * @return 请求结果
     */
    public String funcPost(String userAddress, String funcName, List funcParam)  {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",userAddress);
        jsonObject.put("funcName",funcName);
        if (!funcParam.isEmpty()) {
            jsonObject.put("funcParam", funcParam);
        }
        jsonObject.put("contractAbi",JSONUtil.parseArray(ABI));
        jsonObject.put("contractAddress","0xcf1b2427b15fee5139be4a77e4d19f5b101fe258");
        jsonObject.put("contractName","MySupplyChain");
        jsonObject.put("groupId",1);
        String s = JSONUtil.toJsonStr(jsonObject);

        // 创建httpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post请求方式实例
        HttpPost httpPost = new HttpPost(webaseUrl);
        // 设置请求头 发送的是json数据格式
        httpPost.setHeader("Content-type","application/json;charset=utf-8");
        // 设置参数---设置消息实体 也就是携带的数据
        StringEntity stringEntity = new StringEntity(s,Charset.forName("UTF-8"));
        // 设置编码格式
        stringEntity.setContentEncoding("UTF-8");
        // 发送Json格式的数据请求
        stringEntity.setContentType("application/json");
        // 把请求消息实体塞进去
        httpPost.setEntity(stringEntity);
        // 执行http的post请求
        String result = null;
        CloseableHttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
