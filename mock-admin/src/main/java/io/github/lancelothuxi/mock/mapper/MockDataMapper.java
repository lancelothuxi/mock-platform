package io.github.lancelothuxi.mock.mapper;

import java.util.List;

import io.github.lancelothuxi.mock.domain.MockConfig;
import io.github.lancelothuxi.mock.domain.MockData;
import org.apache.ibatis.annotations.Param;

/**
 * mock配置关联响应数据Mapper接口
 * 
 * @author lancelot huxisuz@gmail.com
 * @since 2023-05-10
 */
public interface MockDataMapper 
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
     * 删除mock配置关联响应数据
     * 
     * @param id mock配置关联响应数据主键
     * @return 结果
     */
    public int deleteMockDataById(Long id);

    /**
     * 批量删除mock配置关联响应数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMockDataByIds(String[] ids);

    List<MockData> selectListByConfigList(@Param("ids") List<String> ids);

    List<MockData> selectByConfigId(@Param("configId") String configId);

    void batchUpdateMockDelayTimeByMockId(@Param("mockConfigId")String mockConfigId, @Param("timeout")Integer timeout);
}
