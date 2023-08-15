package com.lancelot.mock.mock;

import com.alibaba.fastjson.JSON;
import com.lancelot.mock.api.CommonDubboMockService;
import com.lancelot.mock.api.MockRequest;
import com.lancelot.mock.api.MockResponse;
import com.lancelot.mock.cache.LocalCache;
import com.lancelot.mock.domain.MockConfig;
import com.lancelot.mock.functions.CompoundVariable;
import com.lancelot.mock.common.utils.StringUtils;
import com.lancelot.mock.domain.MockData;
import com.lancelot.mock.service.IMockConfigService;
import com.lancelot.mock.service.IMockDataService;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@DubboService(timeout = 5000)
public class MockDubboServiceImpl implements CommonDubboMockService {

    private static Logger logger= LoggerFactory.getLogger(MockDubboServiceImpl.class);

    @Autowired
    private IMockConfigService mockConfigService;

    @Autowired
    private IMockDataService mockDataService;

    @Autowired
    private LocalCache localCache;

    @Override
    public MockResponse doMockRequest(MockRequest mockRequest) {

        String interfaceName = mockRequest.getInterfaceName();
        String methodName = mockRequest.getMethodName();

        long startTime = System.currentTimeMillis();

       try {

           if(logger.isDebugEnabled()){
               logger.debug("doMockRequest mockRequest={}", JSON.toJSONString(mockRequest));
           }

           MockConfig request =new MockConfig();
           request.setType("dubbo");
           request.setInterfaceName(interfaceName);
           request.setMethodName(methodName);
           request.setGroupName(mockRequest.getGroupName());
           request.setVersion(mockRequest.getVersion());
           request.setAppliactionName(mockRequest.getAppName());
           request.setEnabled("1");

           //查询配置
           MockConfig mockConfig = localCache.getMockConfigWithCache(request);
           if(mockConfig == null){
               if(logger.isDebugEnabled()){
                   logger.debug("doMockRequest mockConfigs null mockRequest={}", JSON.toJSONString(mockRequest));
               }
               MockResponse mockResponse=new MockResponse();
               mockResponse.setCode(-1);
               return mockResponse;
           }

           //匹配数据
           MockData mockData = MockUtil.getMockData(mockRequest.getArgs(),mockConfig,mockConfig.getMockDataList());
           if(mockData==null){
               if(logger.isDebugEnabled()){
                   logger.debug("doMockRequest null mockRequest={}", JSON.toJSONString(mockRequest));
               }
               MockResponse mockResponse=new MockResponse();
               mockResponse.setCode(-1);
               return mockResponse;
           }

           String data = mockData.getData();
           if(StringUtils.isEmpty(data)){
               if(logger.isDebugEnabled()){
                   logger.debug("doMockRequest isEmpty mockRequest={}", JSON.toJSONString(mockRequest));
               }
               MockResponse mockResponse=new MockResponse();
               mockResponse.setCode(-1);
               return mockResponse;
           }

           CompoundVariable compoundVariable= new CompoundVariable();
           compoundVariable.setParameters(data);
           data=compoundVariable.execute(mockRequest.getArgs());
           MockResponse mockResponse=new MockResponse();
           mockResponse.setData(data);

           Integer timeout = mockData.getTimeout();
           if(timeout!=null  && timeout>0){
               MockUtil.sleep(timeout,System.currentTimeMillis()-startTime);
           }

           return mockResponse;
       }catch (Exception ex){
           logger.error("doMockRequest error mockRequest={}", JSON.toJSONString(mockRequest),ex);
           MockResponse mockResponse=new MockResponse();
           mockResponse.setCode(-1);
           return mockResponse;
       }
    }
}
