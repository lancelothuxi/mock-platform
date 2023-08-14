package com.lancelot.mock.api.controller.service.impl;

import com.lancelot.mock.api.controller.cache.LocalCache;
import com.lancelot.mock.api.controller.domain.MockConfig;
import com.lancelot.mock.api.controller.mapper.MockConfigMapper;
import com.lancelot.mock.api.controller.service.IMockConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

import java.util.List;

/**
 * 服务mock方法Service业务层处理
 *
 * @author ruoyi
 * @date 2023-05-11
 */
@Service
public class MockConfigServiceImpl implements IMockConfigService
{
    @Autowired
    private MockConfigMapper mockConfigMapper;

    @Autowired
    LocalCache localCache;

    /**
     * 查询服务mock方法
     *
     * @param id 服务mock方法主键
     * @return 服务mock方法
     */
    @Override
    public MockConfig selectMockConfigById(Long id)
    {
        return mockConfigMapper.selectMockConfigById(id);
    }

    /**
     * 查询服务mock方法列表
     *
     * @param mockConfig 服务mock方法
     * @return 服务mock方法
     */
    @Override
    public List<MockConfig> selectMockConfigList(MockConfig mockConfig)
    {
        return mockConfigMapper.selectMockConfigList(mockConfig);
    }

    @Override
    public List<MockConfig> fuzzlySelectMockConfigList(MockConfig mockConfig) {
        return mockConfigMapper.fuzzlySelectMockConfigList(mockConfig);
    }

    /**
     * 查询服务mock方法
     *
     * @param mockConfig 服务mock方法
     * @return 服务mock方法
     */
    @Override
    public MockConfig selectMockConfig(MockConfig mockConfig)
    {

        return mockConfigMapper.selectMockConfig(mockConfig);
    }


    @Override
    public String queryMockConfigData(MockConfig mockConfig) {
        return mockConfigMapper.selectMockConfigData(mockConfig);
    }

    /**
     * 新增服务mock方法
     *
     * @param mockConfig 服务mock方法
     * @return 结果
     */
    @Override
    public Long insertMockConfig(MockConfig mockConfig)
    {
        localCache.mockConfigAdd(mockConfig);
        return mockConfigMapper.insertMockConfig(mockConfig);
    }

    /**
     * 修改服务mock方法
     *
     * @param mockConfig 服务mock方法
     * @return 结果
     */
    @Override
    public int updateMockConfig(MockConfig mockConfig)
    {
        int updateMockConfig = mockConfigMapper.updateMockConfig(mockConfig);

        MockConfig real = mockConfigMapper.selectMockConfigById(mockConfig.getId());

        localCache.mockConfigUpdate(real);

        return updateMockConfig;
    }

    /**
     * 批量删除服务mock方法
     *
     * @param ids 需要删除的服务mock方法主键
     * @return 结果
     */
    @Override
    public int deleteMockConfigByIds(String ids)
    {
        String[] idArray = Convert.toStrArray(ids);
        for (String s : idArray) {
            MockConfig real = mockConfigMapper.selectMockConfigById(Long.valueOf(s));
            localCache.mockConfigDelete(real);
        }

        return mockConfigMapper.deleteMockConfigByIds(idArray);
    }

    /**
     * 删除服务mock方法信息
     *
     * @param id 服务mock方法主键
     * @return 结果
     */
    @Override
    public int deleteMockConfigById(Long id)
    {

        MockConfig mockConfig = mockConfigMapper.selectMockConfigById(id);
        if(mockConfig==null){
            return 0;
        }

        int deleteMockConfigById = mockConfigMapper.deleteMockConfigById(id);

        localCache.mockConfigDelete(mockConfig);

        return deleteMockConfigById;

    }

    /**
     * 批量配置mock规则
     * @param MockConfigList
     * @return
     */
    @Override
    public int batchConfigMockConfig(List<MockConfig> MockConfigList) {
        return mockConfigMapper.batchConfigMock(MockConfigList);
    }
}
