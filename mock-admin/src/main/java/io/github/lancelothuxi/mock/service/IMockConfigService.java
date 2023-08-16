package io.github.lancelothuxi.mock.service;

import io.github.lancelothuxi.mock.domain.MockConfig;

import java.util.List;

/**
 * 服务mock方法Service接口
 * 
 * @author lancelot huxisuz@gmail.com
 * @date 2023-05-11
 */
public interface IMockConfigService 
{
    /**
     * 查询服务mock方法
     * 
     * @param id 服务mock方法主键
     * @return 服务mock方法
     */
    public MockConfig selectMockConfigById(Long id);



        /**
         * 查询服务mock方法列表
         *
         * @param mockConfig 服务mock方法
         * @return 服务mock方法集合
         */
    public List<MockConfig> selectMockConfigList(MockConfig mockConfig);


    public List<MockConfig> fuzzlySelectMockConfigList(MockConfig mockConfig);

    /**
     * 查询服务mock方法
     *
     * @param mockConfig 服务mock方法
     * @return 服务mock方法集合
     */
    public MockConfig selectMockConfig(MockConfig mockConfig);

    /**
     * 查询服务mock方法
     *
     * @return mock的data数据
     */
    public String queryMockConfigData(MockConfig mockConfig);

    /**
     * 新增服务mock方法
     * 
     * @param mockConfig 服务mock方法
     * @return 结果
     */
    public Long insertMockConfig(MockConfig mockConfig);

    /**
     * 修改服务mock方法
     * 
     * @param mockConfig 服务mock方法
     * @return 结果
     */
    public int updateMockConfig(MockConfig mockConfig);

    /**
     * 批量删除服务mock方法
     * 
     * @param ids 需要删除的服务mock方法主键集合
     * @return 结果
     */
    public int deleteMockConfigByIds(String ids);

    /**
     * 删除服务mock方法信息
     * 
     * @param id 服务mock方法主键
     * @return 结果
     */
    public int deleteMockConfigById(Long id);

    /**
     * 批量配置mock规则
     * @param MockConfigList
     * @return
     */
    public int batchConfigMockConfig(List<MockConfig> MockConfigList);
}
