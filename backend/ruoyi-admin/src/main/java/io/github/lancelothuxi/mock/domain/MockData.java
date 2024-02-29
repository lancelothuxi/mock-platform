package io.github.lancelothuxi.mock.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * mock数据对象 mock_data
 *
 * @author ruoyi
 * @date 2024-02-06
 */
@Data
public class MockData {
    /**
     * 主键
     */
    private Long id;

    /**
     * mock响应数据值
     */
    @Excel(name = "mock响应数据值")
    private String data;

    /**
     * 指定超时时间
     */
    @Excel(name = "指定超时时间")
    private Long timeout;

    /**
     * 是否启用
     */
    @Excel(name = "是否启用")
    private int enabled;

    /**
     * mock规则配置表的id
     */
    private Long mockConfigId;

    /**
     * 匹配规则类型
     */
    @Excel(name = "匹配规则类型")
    private String matchType;

    /**
     * 匹配规则
     */
    @Excel(name = "匹配规则")
    private String matchExpression;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

}
