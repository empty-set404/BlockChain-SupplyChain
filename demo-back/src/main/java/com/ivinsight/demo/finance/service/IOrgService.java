package com.ivinsight.demo.finance.service;


import com.ivinsight.demo.finance.model.Result;
import com.ivinsight.demo.finance.model.bo.LoginBO;
import com.ivinsight.demo.finance.model.bo.RegisterBO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Arthur
 * @since 2021-09-07
 */
public interface IOrgService {
    Result login(@RequestBody LoginBO loginBO);
    Result<String> register(RegisterBO registerBO);
    Result getCompany(String userAddress);
}
