package com.lancelot.mock.api.controller.service;

import com.lancelot.mock.api.controller.domain.MockConfig;
import com.lancelot.mock.api.controller.domain.MockData;

import java.util.List;


/**
 * mock配置关联响应数据Service接口
 * 
 * @author ruoyi
 * @date 2023-05-10
 */
public interface IMockDataService 
{
    /**
     * 查询mock配置关联响应数据
     * 
     * @param id mock配置关联响应数据主键
     * @return mock配置关联响应数据
     */
    public MockData selectMockDataById(Long id);

    /**
     * 查询mock配置关联响应数据列表
     * 
     * @param mockData mock配置关联响应数据
     * @return mock配置关联响应数据集合
     */
    public List<MockData> selectMockDataList(MockData mockData);

    /**
     * 更加mockConfig配置数据
     * @param mockConfig
     * @return
     */
    public MockData selectMockDataResult(MockConfig mockConfig);

    /**
     * 新增mock配置关联响应数据
     * 
     * @param mockData mock配置关联响应数据
     * @return 结果
     */
    public int insertMockData(MockData mockData);

    /**
     * 修改mock配置关联响应数据
     * 
     * @param mockData mock配置关联响应数据
     * @return 结果
     */
    public int updateMockData(MockData mockData);

    /**
     * 批量删除mock配置关联响应数据
     * 
     * @param ids 需要删除的mock配置关联响应数据主键集合
     * @return 结果
     */
    public int deleteMockDataByIds(String ids);

    /**
     * 删除mock配置关联响应数据信息
     * 
     * @param id mock配置关联响应数据主键
     * @return 结果
     */
    public int deleteMockDataById(Long id);

    List<MockData> selectListByConfigList(List<String> ids);

    List<MockData> selectByConfigId(String configId);

    void importMockData(List<MockData> mockDatas, String mockConfigId);

    void batchUpdateMockDelayTimeByMockId(String mockConfigId, Integer timeout);
}
