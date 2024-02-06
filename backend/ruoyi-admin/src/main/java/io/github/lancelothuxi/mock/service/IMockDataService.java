package io.github.lancelothuxi.mock.service;

import java.util.List;

import io.github.lancelothuxi.mock.domain.MockData;

/**
 * mock数据Service接口
 *
 * @author ruoyi
 * @date 2024-02-06
 */
public interface IMockDataService {
    /**
     * 查询mock数据
     *
     * @param id mock数据主键
     * @return mock数据
     */
    public MockData selectMockDataById(Long id);

    /**
     * 查询mock数据列表
     *
     * @param mockData mock数据
     * @return mock数据集合
     */
    public List<MockData> selectMockDataList(MockData mockData);

    /**
     * 新增mock数据
     *
     * @param mockData mock数据
     * @return 结果
     */
    public int insertMockData(MockData mockData);

    /**
     * 修改mock数据
     *
     * @param mockData mock数据
     * @return 结果
     */
    public int updateMockData(MockData mockData);

    /**
     * 批量删除mock数据
     *
     * @param ids 需要删除的mock数据主键集合
     * @return 结果
     */
    public int deleteMockDataByIds(Long[] ids);

    /**
     * 删除mock数据信息
     *
     * @param id mock数据主键
     * @return 结果
     */
    public int deleteMockDataById(Long id);
}
