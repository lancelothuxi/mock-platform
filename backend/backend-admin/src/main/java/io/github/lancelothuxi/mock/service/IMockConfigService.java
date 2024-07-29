package io.github.lancelothuxi.mock.service;

import java.util.List;

import io.github.lancelothuxi.mock.domain.MockConfig;

/**
 * mock配置Service接口
 *
 * @author huxisuz@gmail.com
 * @date 2024-02-06
 */
public interface IMockConfigService {
    /**
     * 查询mock配置
     *
     * @param id mock配置主键
     * @return mock配置
     */
    public MockConfig selectMockConfigById(Long id);

    /**
     * 查询mock配置列表
     *
     * @param mockConfig mock配置
     * @return mock配置集合
     */
    public List<MockConfig> selectMockConfigList(MockConfig mockConfig);


    public List<MockConfig> query4Page(MockConfig mockConfig);

    /**
     * 新增mock配置
     *
     * @param mockConfig mock配置
     * @return 结果
     */
    public int insertMockConfig(MockConfig mockConfig);

    /**
     * 修改mock配置
     *
     * @param mockConfig mock配置
     * @return 结果
     */
    public int updateMockConfig(MockConfig mockConfig);

    /**
     * 批量删除mock配置
     *
     * @param ids 需要删除的mock配置主键集合
     * @return 结果
     */
    public int deleteMockConfigByIds(Long[] ids);

    /**
     * 删除mock配置信息
     *
     * @param id mock配置主键
     * @return 结果
     */
    public int deleteMockConfigById(Long id);

    MockConfig selectMockConfig(MockConfig clientConfig);
}
