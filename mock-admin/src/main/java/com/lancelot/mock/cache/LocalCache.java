package com.lancelot.mock.cache;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lancelot.mock.domain.MockConfig;
import com.lancelot.mock.domain.MockData;
import com.lancelot.mock.mock.MockExpression;
import com.lancelot.mock.service.IMockConfigService;
import com.lancelot.mock.service.IMockDataService;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/7/18 下午2:49
 */
@Component
public class LocalCache   {

    private Cache<String, MockConfig> mockConfigCache;

    private Cache<String, List<MockData>> mockDatasCache;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    IMockConfigService mockConfigService;

    @Autowired
    IMockDataService mockDataService;

    @PostConstruct
    private void init(){
        mockConfigCache=Caffeine.newBuilder()
                .maximumSize(Long.MAX_VALUE)
                .build();



        //启动吧数据库所有enabled mock数据加载到本地缓存
        MockConfig query=new MockConfig();
        query.setEnabled("1");
        List<MockConfig> mockConfigs = mockConfigService.selectMockConfigList(query);
        for (MockConfig mockConfig : mockConfigs) {
            addToCache(mockConfig);
        }

        RTopic mockConfigChangedTopic = redissonClient.getTopic("mockConfigChanged");
        // 订阅消息
        mockConfigChangedTopic.addListener(String.class,
                (channel, message) -> {

                    MockConfigCacheUpdateMessage request = JSON.parseObject(message, MockConfigCacheUpdateMessage.class);

                    String op = request.getOp();

                    String cacheKey = CacheUtil.buildKey4Config(request.getAppliactionName(), request.getType(), request.getAppliactionName(),
                            request.getMethodName(), request.getGroupName(), request.getVersion());

                    if(OperationConstant.ADD.equals(op)){
                        MockConfig mockConfigRequest=new MockConfig();
                        mockConfigRequest.setType(request.getType());
                        mockConfigRequest.setInterfaceName(request.getInterfaceName());
                        mockConfigRequest.setMethodName(request.getMethodName());
                        mockConfigRequest.setGroupName(request.getGroupName());
                        mockConfigRequest.setVersion(request.getVersion());
                        mockConfigRequest.setEnabled("1");
                        mockConfigRequest.setAppliactionName(request.getAppliactionName());

                        MockConfig real = mockConfigService.selectMockConfig(mockConfigRequest);
                        if(real!=null){
                            addToCache(real);
                        }

                        return;
                    }

                    if(OperationConstant.DELETE.equals(op)){
                        mockConfigCache.invalidate(cacheKey);
                        return;
                    }

                    if(OperationConstant.UPDATE.equals(op)){

                        mockConfigCache.invalidate(cacheKey);

                        MockConfig mockConfigRequest=new MockConfig();
                        mockConfigRequest.setType(request.getType());
                        mockConfigRequest.setInterfaceName(request.getInterfaceName());
                        mockConfigRequest.setMethodName(request.getMethodName());
                        mockConfigRequest.setGroupName(request.getGroupName());
                        mockConfigRequest.setVersion(request.getVersion());
                        mockConfigRequest.setEnabled("1");
                        mockConfigRequest.setAppliactionName(request.getAppliactionName());

                        MockConfig real = mockConfigService.selectMockConfig(mockConfigRequest);
                        if(real!=null){
                            addToCache(real);
                        }

                        return;
                    }

                });


    }
    private void addToCache(MockConfig mockConfig) {
        String cacheKey = CacheUtil.buildKey4Config(mockConfig.getAppliactionName(), mockConfig.getType(), mockConfig.getAppliactionName(),
                mockConfig.getMethodName(), mockConfig.getGroupName(), mockConfig.getVersion());
        List<MockData> mockDataList = mockDataService.selectByConfigId(mockConfig.getId() + "");
        mockConfig.setMockDataList(mockDataList);


        if(mockConfig.getDirectMatch()!=null && mockConfig.getDirectMatch()==1){
            for (MockData mockData : mockDataList) {
                List<MockExpression> mockExpressions = mockData.getMockExpressions();
                for (MockExpression mockExpression : mockExpressions) {
                    mockConfig.getDirMockDataMap().put(mockExpression.getExpectedValue(),mockData);
                }
            }
        }
        mockConfigCache.put(cacheKey,mockConfig);
    }


    public MockConfig getMockConfigWithCache(MockConfig request){

        String cacheKey = CacheUtil.buildKey4Config(request.getAppliactionName(), request.getType(), request.getAppliactionName(),
                request.getMethodName(), request.getGroupName(), request.getVersion());

        MockConfig mockConfig = mockConfigCache.getIfPresent(cacheKey);
        if(mockConfig==null){
            request.setEnabled("1");
            MockConfig real = mockConfigService.selectMockConfig(request);
            if(real!=null){
                List<MockData> mockDataList = mockDataService.selectByConfigId(real.getId() + "");
                real.setMockDataList(mockDataList);
                mockConfigCache.put(cacheKey,real);
            }
            return real;
        }else {
            return mockConfig;
        }
    }

    /**
     * 删除
     * @param request
     */
    public void mockConfigDelete(MockConfig request){
        RTopic mockConfigChangedTopic = redissonClient.getTopic("mockConfigChanged");
        MockConfigCacheUpdateMessage mockConfigCacheUpdateMessage = convertToMessag(request, OperationConstant.ADD);

        mockConfigCacheUpdateMessage.setOp(OperationConstant.DELETE);

        mockConfigChangedTopic.publish(JSON.toJSONString(mockConfigCacheUpdateMessage));
    }


    /**
     * 新增
     * @param request
     */
    public void mockConfigAdd(MockConfig request){
        RTopic mockConfigChangedTopic = redissonClient.getTopic("mockConfigChanged");

        MockConfigCacheUpdateMessage mockConfigCacheUpdateMessage = convertToMessag(request, OperationConstant.ADD);

        mockConfigChangedTopic.publish(JSON.toJSONString(mockConfigCacheUpdateMessage));
    }

    /**
     * 跟你新年
     * @param request
     */
    public void mockConfigUpdate(MockConfig request){
        RTopic mockConfigChangedTopic = redissonClient.getTopic("mockConfigChanged");

        MockConfigCacheUpdateMessage mockConfigCacheUpdateMessage = convertToMessag(request, OperationConstant.UPDATE);

        mockConfigChangedTopic.publish(JSON.toJSONString(mockConfigCacheUpdateMessage));
    }


    private MockConfigCacheUpdateMessage convertToMessag(MockConfig request, String add) {
        MockConfigCacheUpdateMessage mockConfigCacheUpdateMessage = new MockConfigCacheUpdateMessage();
        mockConfigCacheUpdateMessage.setOp(add);
        mockConfigCacheUpdateMessage.setId(request.getId());
        mockConfigCacheUpdateMessage.setInterfaceName(request.getInterfaceName());
        mockConfigCacheUpdateMessage.setMethodName(request.getMethodName());
        mockConfigCacheUpdateMessage.setGroupName(request.getGroupName());
        mockConfigCacheUpdateMessage.setVersion(request.getVersion());
        mockConfigCacheUpdateMessage.setAppliactionName(request.getAppliactionName());
        mockConfigCacheUpdateMessage.setType(request.getType());
        return mockConfigCacheUpdateMessage;
    }

}
