package io.github.lancelothuxi.mock.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * mock数据对象 mock_data
 * 
 * @author ruoyi
 * @date 2024-02-06
 */
public class MockData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** mock响应数据值 */
    @Excel(name = "mock响应数据值")
    private String data;

    /** 指定超时时间 */
    @Excel(name = "指定超时时间")
    private Long timeout;

    /** 是否启用 */
    @Excel(name = "是否启用")
    private Long enabled;

    /** mock规则配置表的id */
    private Long mockConfigId;

    /** 匹配规则类型 */
    @Excel(name = "匹配规则类型")
    private String matchType;

    /** 匹配规则 */
    @Excel(name = "匹配规则")
    private String matchExpression;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    private Date updatedTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }
    public void setTimeout(Long timeout) 
    {
        this.timeout = timeout;
    }

    public Long getTimeout() 
    {
        return timeout;
    }
    public void setEnabled(Long enabled) 
    {
        this.enabled = enabled;
    }

    public Long getEnabled() 
    {
        return enabled;
    }
    public void setMockConfigId(Long mockConfigId) 
    {
        this.mockConfigId = mockConfigId;
    }

    public Long getMockConfigId() 
    {
        return mockConfigId;
    }
    public void setMatchType(String matchType) 
    {
        this.matchType = matchType;
    }

    public String getMatchType() 
    {
        return matchType;
    }
    public void setMatchExpression(String matchExpression) 
    {
        this.matchExpression = matchExpression;
    }

    public String getMatchExpression() 
    {
        return matchExpression;
    }
    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }
    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("data", getData())
            .append("timeout", getTimeout())
            .append("enabled", getEnabled())
            .append("mockConfigId", getMockConfigId())
            .append("matchType", getMatchType())
            .append("matchExpression", getMatchExpression())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
