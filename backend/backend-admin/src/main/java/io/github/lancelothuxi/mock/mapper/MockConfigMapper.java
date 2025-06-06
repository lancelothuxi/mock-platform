package io.github.lancelothuxi.mock.mapper;

import java.util.List;

import io.github.lancelothuxi.mock.domain.MockConfig;

/**
 * mock配置Mapper接口
 *
 * @author huxisuz@gmail.com
 * @date 2024-02-06
 */
public interface MockConfigMapper {
    /**
     * 查询mock配置
     *
     * @param id mock配置主键
     * @return mock配置
     */
    MockConfig selectMockConfigById(Long id);

    /**
     * 查询mock配置列表
     *
     * @param mockConfig mock配置
     * @return mock配置集合
     */
    List<MockConfig> query4Page(MockConfig mockConfig);


    List<MockConfig> selectMockConfigList(MockConfig mockConfig);

    /**
     * 新增mock配置
     *
     * @param mockConfig mock配置
     * @return 结果
     */
    int insertMockConfig(MockConfig mockConfig);

    /**
     * 修改mock配置
     *
     * @param mockConfig mock配置
     * @return 结果
     */
    int updateMockConfig(MockConfig mockConfig);

    /**
     * 删除mock配置
     *
     * @param id mock配置主键
     * @return 结果
     */
    int deleteMockConfigById(Long id);

    /**
     * 批量删除mock配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMockConfigByIds(Long[] ids);

    MockConfig selectMockConfig(MockConfig clientConfig);
}
