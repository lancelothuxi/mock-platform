package io.github.lancelothuxi.mock.service.impl;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.cache.LocalCache;
import io.github.lancelothuxi.mock.domain.MockConfig;
import io.github.lancelothuxi.mock.mapper.MockDataMapper;
import io.github.lancelothuxi.mock.mock.MockExpression;
import io.github.lancelothuxi.mock.service.IMockConfigService;
import io.github.lancelothuxi.mock.service.IMockDataService;
import io.github.lancelothuxi.mock.common.core.text.Convert;
import io.github.lancelothuxi.mock.common.utils.StringUtils;
import io.github.lancelothuxi.mock.domain.MockData;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * mock配置关联响应数据Service业务层处理
 *
 * @author lancelot huxisuz@gmail.com
 * @date 2023-05-10
 */
@Service
public class MockDataServiceImpl implements IMockDataService {

    private static Logger logger= LoggerFactory.getLogger(MockDataServiceImpl.class);

    @Autowired
    private MockDataMapper mockDataMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IMockConfigService mockConfigService;

    @Autowired
    LocalCache localCache;

    /**
     * 查询mock配置关联响应数据
     *
     * @param id mock配置关联响应数据主键
     * @return mock配置关联响应数据
     */
    @Override
    public MockData selectMockDataById(Long id) {
        return mockDataMapper.selectMockDataById(id);
    }

    /**
     * 查询mock配置关联响应数据列表
     *
     * @param mockData mock配置关联响应数据
     * @return mock配置关联响应数据
     */
    @Override
    public List<MockData> selectMockDataList(MockData mockData) {
        return mockDataMapper.selectMockDataList(mockData);
    }

    /**
     * 新增mock配置关联响应数据
     *
     * @param mockData mock配置关联响应数据
     * @return 结果
     */
    @Override
    public int insertMockData(MockData mockData) {

        int insertMockData = mockDataMapper.insertMockData(mockData);

        String mockConfigId = mockData.getMockConfigId();

        MockConfig mockConfig = mockConfigService.selectMockConfigById(Long.valueOf(mockConfigId));
        localCache.mockConfigUpdate(mockConfig);

        return insertMockData;
    }

    /**
     * 修改mock配置关联响应数据
     *
     * @param mockData mock配置关联响应数据
     * @return 结果
     */
    @Override
    public int updateMockData(MockData mockData) {

        int updateMockData = mockDataMapper.updateMockData(mockData);

        MockData real = mockDataMapper.selectMockDataById(mockData.getId());

        MockConfig mockConfig = mockConfigService.selectMockConfigById(Long.valueOf(real.getMockConfigId()));
        localCache.mockConfigUpdate(mockConfig);
        return updateMockData;

    }

    /**
     * 批量删除mock配置关联响应数据
     *
     * @param ids 需要删除的mock配置关联响应数据主键
     * @return 结果
     */
    @Override
    public int deleteMockDataByIds(String ids) {

        String[] array = Convert.toStrArray(ids);
        for (String id : array) {
            MockData mockDataById = selectMockDataById(Long.valueOf(id));
            MockConfig mockConfig = mockConfigService.selectMockConfigById(Long.valueOf(mockDataById.getMockConfigId()));
            localCache.mockConfigUpdate(mockConfig);
        }

        return mockDataMapper.deleteMockDataByIds(array);
    }

    /**
     * 删除mock配置关联响应数据信息
     *
     * @param id mock配置关联响应数据主键
     * @return 结果
     */
    @Override
    public int deleteMockDataById(Long id) {

        MockData mockDataById = selectMockDataById(Long.valueOf(id));
        mockDataMapper.deleteMockDataById(Long.valueOf(id));

        MockConfig mockConfig = mockConfigService.selectMockConfigById(Long.valueOf(mockDataById.getMockConfigId()));
        localCache.mockConfigUpdate(mockConfig);
        return 1;
    }

    @Override
    public List<MockData> selectListByConfigList(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return mockDataMapper.selectListByConfigList(ids);
    }

    @Override
    public List<MockData> selectByConfigId(String configId) {
        List<MockData> mockDataList = mockDataMapper.selectByConfigId(configId);
        for (MockData mockData : mockDataList) {
            String mockReqParams = mockData.getMockReqParams();
            if(StringUtils.isEmpty(mockReqParams)){
                continue;
            }

            try {
                List<MockExpression> mockExpressions = JSON.parseArray(mockReqParams, MockExpression.class);
                mockData.setMockExpressions(mockExpressions);
            }catch (Exception ex){
                logger.error("parse mockReqParams to MockExpression list error, mockReqParams={},configId={}",mockReqParams,configId,ex);
            }
        }

        return mockDataList;
    }

    @Override
    public void importMockData(List<MockData> mockDatas, String mockConfigId) {
        if (mockDatas==null || mockDatas.size() == 0) {
            return;
        }
        for (MockData mockData : mockDatas) {
            mockData.setMockConfigId(mockConfigId);
            mockData.setCreatedTime(new Date());
            mockData.setUpdatedTime(new Date());
            mockDataMapper.insertMockData(mockData);
        }
    }

    @Override
    public void batchUpdateMockDelayTimeByMockId(String mockConfigId, Integer timeout) {
        mockDataMapper.batchUpdateMockDelayTimeByMockId(mockConfigId,timeout);
    }
}
