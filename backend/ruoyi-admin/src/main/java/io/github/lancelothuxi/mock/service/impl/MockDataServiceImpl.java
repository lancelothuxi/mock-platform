package io.github.lancelothuxi.mock.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.lancelothuxi.mock.mapper.MockDataMapper;
import io.github.lancelothuxi.mock.domain.MockData;
import io.github.lancelothuxi.mock.service.IMockDataService;

/**
 * mock数据Service业务层处理
 *
 * @author ruoyi
 * @date 2024-02-06
 */
@Service
public class MockDataServiceImpl implements IMockDataService {
    @Autowired
    private MockDataMapper mockDataMapper;

    /**
     * 查询mock数据
     *
     * @param id mock数据主键
     * @return mock数据
     */
    @Override
    public MockData selectMockDataById(Long id) {
        return mockDataMapper.selectMockDataById(id);
    }

    /**
     * 查询mock数据列表
     *
     * @param mockData mock数据
     * @return mock数据
     */
    @Override
    public List<MockData> selectMockDataList(MockData mockData) {
        return mockDataMapper.selectMockDataList(mockData);
    }

    /**
     * 新增mock数据
     *
     * @param mockData mock数据
     * @return 结果
     */
    @Override
    public int insertMockData(MockData mockData) {
        return mockDataMapper.insertMockData(mockData);
    }

    /**
     * 修改mock数据
     *
     * @param mockData mock数据
     * @return 结果
     */
    @Override
    public int updateMockData(MockData mockData) {
        return mockDataMapper.updateMockData(mockData);
    }

    /**
     * 批量删除mock数据
     *
     * @param ids 需要删除的mock数据主键
     * @return 结果
     */
    @Override
    public int deleteMockDataByIds(Long[] ids) {
        return mockDataMapper.deleteMockDataByIds(ids);
    }

    /**
     * 删除mock数据信息
     *
     * @param id mock数据主键
     * @return 结果
     */
    @Override
    public int deleteMockDataById(Long id) {
        return mockDataMapper.deleteMockDataById(id);
    }
}
