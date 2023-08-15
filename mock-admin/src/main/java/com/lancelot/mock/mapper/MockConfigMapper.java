package com.lancelot.mock.mapper;

import com.lancelot.mock.domain.MockConfig;

import java.util.List;

/**
 * 服务mock方法Mapper接口
 * 
 * @author ruoyi
 * @date 2023-05-11
 */
public interface MockConfigMapper 
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

    /**
     * 查询服务mock方法
     *
     * @param mockConfig 服务mock方法
     * @return 服务mock方法集合
     */
    public MockConfig selectMockConfig(MockConfig mockConfig);

    /**
     * 匹配Mock的data
     *
     * @param mockConfig
     * @return
     */
    public String selectMockConfigData(MockConfig mockConfig);

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
     * 删除服务mock方法
     * 
     * @param id 服务mock方法主键
     * @return 结果
     */
    public int deleteMockConfigById(Long id);

    /**
     * 批量删除服务mock方法
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMockConfigByIds(String[] ids);


    /**
     * 批量配置mock
     * @param MockConfigList
     * @return
     */
    public int batchConfigMock(List<MockConfig> MockConfigList);


    List<MockConfig> fuzzlySelectMockConfigList(MockConfig mockConfig);
}
