package io.github.lancelothuxi.mock.mock;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.api.MockRequest;
import io.github.lancelothuxi.mock.api.MockResponse;
import io.github.lancelothuxi.mock.cache.LocalCache;
import io.github.lancelothuxi.mock.common.utils.StringUtils;
import io.github.lancelothuxi.mock.domain.MockConfig;
import io.github.lancelothuxi.mock.domain.MockData;
import io.github.lancelothuxi.mock.functions.CompoundVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/9/7 上午11:56
 */
@Service
public class CommonMockService {

    @Autowired
    private LocalCache localCache;

    private static Logger logger= LoggerFactory.getLogger(CommonMockService.class);

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
