package io.github.lancelothuxi.mock.mapper;

import java.util.List;
import io.github.lancelothuxi.mock.domain.MockData;

/**
 * mock数据Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-06
 */
public interface MockDataMapper 
{
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
     * 删除mock数据
     * 
     * @param id mock数据主键
     * @return 结果
     */
    public int deleteMockDataById(Long id);

    /**
     * 批量删除mock数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMockDataByIds(Long[] ids);
}
