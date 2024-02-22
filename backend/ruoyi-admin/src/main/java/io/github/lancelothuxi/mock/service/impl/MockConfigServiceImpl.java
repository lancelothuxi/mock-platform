package io.github.lancelothuxi.mock.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.lancelothuxi.mock.mapper.MockConfigMapper;
import io.github.lancelothuxi.mock.domain.MockConfig;
import io.github.lancelothuxi.mock.service.IMockConfigService;

/**
 * mock配置Service业务层处理
 *
 * @author huxisuz@gmail.com
 * @date 2024-02-06
 */
@Service
public class MockConfigServiceImpl implements IMockConfigService {
    @Autowired
    private MockConfigMapper mockConfigMapper;

    /**
     * 查询mock配置
     *
     * @param id mock配置主键
     * @return mock配置
     */
    @Override
    public MockConfig selectMockConfigById(Long id) {
        return mockConfigMapper.selectMockConfigById(id);
    }

    /**
     * 查询mock配置列表
     *
     * @param mockConfig mock配置
     * @return mock配置
     */
    @Override
    public List<MockConfig> selectMockConfigList(MockConfig mockConfig) {
        return mockConfigMapper.selectMockConfigList(mockConfig);
    }

    @Override
    public List<MockConfig> query4Page(MockConfig mockConfig) {
        return mockConfigMapper.query4Page(mockConfig);
    }

    /**
     * 新增mock配置
     *
     * @param mockConfig mock配置
     * @return 结果
     */
    @Override
    public int insertMockConfig(MockConfig mockConfig) {
        return mockConfigMapper.insertMockConfig(mockConfig);
    }

    /**
     * 修改mock配置
     *
     * @param mockConfig mock配置
     * @return 结果
     */
    @Override
    public int updateMockConfig(MockConfig mockConfig) {
        return mockConfigMapper.updateMockConfig(mockConfig);
    }

    /**
     * 批量删除mock配置
     *
     * @param ids 需要删除的mock配置主键
     * @return 结果
     */
    @Override
    public int deleteMockConfigByIds(Long[] ids) {
        return mockConfigMapper.deleteMockConfigByIds(ids);
    }

    /**
     * 删除mock配置信息
     *
     * @param id mock配置主键
     * @return 结果
     */
    @Override
    public int deleteMockConfigById(Long id) {
        return mockConfigMapper.deleteMockConfigById(id);
    }

    @Override
    public MockConfig selectMockConfig(MockConfig clientConfig) {
        return mockConfigMapper.selectMockConfig(clientConfig);
    }
}
