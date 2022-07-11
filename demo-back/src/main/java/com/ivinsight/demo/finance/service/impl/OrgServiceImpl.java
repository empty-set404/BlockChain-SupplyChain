package com.ivinsight.demo.finance.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.ivinsight.demo.finance.model.Result;
import com.ivinsight.demo.finance.model.bo.LoginBO;
import com.ivinsight.demo.finance.model.bo.RegisterBO;
import com.ivinsight.demo.finance.model.vo.ResultVO;
import com.ivinsight.demo.finance.service.IOrgService;
import com.ivinsight.demo.finance.utils.IOUtil;
import com.ivinsight.demo.finance.utils.WeBASEUtils;
import io.netty.util.internal.StringUtil;
import org.fisco.bcos.sdk.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Arthur
 * @since 2021-09-07
 */
@Service
public class OrgServiceImpl implements IOrgService {
    @Autowired
    WeBASEUtils weBASEUtils;


    public static final String ABI = IOUtil.readResourceAsString("abi/MySupplyChain.abi");

    /**
     * 登录Service
     * LoginBO loginBO
     **/
    public Result login(@RequestBody LoginBO loginBO) {
        if (StringUtil.isNullOrEmpty(loginBO.getAddress())) {
            return Result.error(ResultVO.PARAM_EMPTY);
        }

        List<Object> objects = new ArrayList<>();
        objects.add(loginBO.getAddress());
        String getCompany = weBASEUtils.funcPost(loginBO.getAddress(), "getCompany", objects);

        JSONArray company = JSONUtil.parseArray(getCompany);
        HashMap<String, Object> _resultMap = new HashMap<>();
        _resultMap.put("companyName",company.get(0));
        _resultMap.put("companyAddress",company.get(1));
        _resultMap.put("creditAsset",company.get(2));
        _resultMap.put("companyType",company.get(3));

        return Result.success(_resultMap);
    }


    /**
     * 注册Service
     * RegisterBO registerBO
     * */
    public Result<String> register(RegisterBO registerBO) {
        if (StrUtil.isEmpty(registerBO.getUsername()) ||
                StrUtil.isEmpty(registerBO.getAddress()) ||
                registerBO.getOrgType() == null
        ) {
            return Result.error(ResultVO.PARAM_EMPTY);
        }

        List funcParam = new ArrayList();
        funcParam.add(registerBO.getUsername());
        funcParam.add(registerBO.getAddress());
        funcParam.add(registerBO.getOrgType());
        funcParam.add(registerBO.getCredit());

        String _result = weBASEUtils.funcPost(registerBO.getAddress(),"addCompany",funcParam);
        JSONObject _resultJson = JSONUtil.parseObj(_result);
        if (_resultJson.getBool("statusOK") != true){ // _resultJson.getInt("code") > 0
            if (_resultJson.getInt("message") == 500002){
                return Result.error(ResultVO.QUERY_EXISTS);
            }
            return Result.error(ResultVO.CONTRACT_ERROR);
        }

        return Result.success("ok");
    }


    public Result getCompany(String userAddress) {
        List funcParam = new ArrayList();
        funcParam.add(userAddress);

        String _result = weBASEUtils.funcPost(userAddress,"getCompany",funcParam);
        JSONArray objArray = JSONUtil.parseArray(_result);
        HashMap _resultMap = new HashMap();
        _resultMap.put("companyName",objArray.getStr(0));
        _resultMap.put("companyAddress",objArray.getStr(1));
        _resultMap.put("creditAsset",objArray.getBigInteger(2));
        _resultMap.put("companyType",objArray.getBigInteger(3));

        return Result.success(_resultMap);
    }

}
