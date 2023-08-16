package io.github.lancelothuxi.mock.domain;

import java.util.Date;
import java.util.List;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.lancelothuxi.mock.mock.MockExpression;
import io.github.lancelothuxi.mock.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * mock配置关联响应数据对象 mock_data
 *
 * @author lancelot huxisuz@gmail.com
 * @date 2023-05-10
 */
public class MockData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** mock规则配置表的id */
//    @ExcelProperty(name = "mock规则配置表的id")
    private String mockConfigId;

    /** mock响应数据值 */
    @ExcelProperty(value = "mock数据", index = 0)
    private String data;

    /** mock请求参数匹配规则 */
    @ExcelProperty(value = "jsonPath", index = 1)
    private String mockReqParams;

    /** 根据jsonpath eval后的实际值 */
//    @ExcelProperty(name = "预期值")
    private String expectedValue;

    /** 超时时间 */
    @ExcelProperty(value = "超时时间", index = 2)
    private Integer timeout;

    /** 服务名 */
//    @ExcelProperty(name = "服务名")
    private String interfaceName;

    /** 方法名 */
//    @ExcelProperty(name = "方法名")
    private String methodName;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
//    @ExcelProperty(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
//    @ExcelProperty(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;


    /** 是否启用 */
    @ExcelProperty(value = "是否启用", index = 3)
    private Long enabled;

    private String applicationName;


    private List<MockExpression> mockExpressions;


    public List<MockExpression> getMockExpressions() {
        return mockExpressions;
    }

    public void setMockExpressions(List<MockExpression> mockExpressions) {
        this.mockExpressions = mockExpressions;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Long getEnabled() {
        return enabled;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setEnabled(Long enabled) {
        this.enabled = enabled;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

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

    public String getMockConfigId() {
        return mockConfigId;
    }

    public void setMockConfigId(String mockConfigId) {
        this.mockConfigId = mockConfigId;
    }

    public String getMockReqParams() {
        return mockReqParams;
    }

    public void setMockReqParams(String mockReqParams) {
        this.mockReqParams = mockReqParams;
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

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("data", getData())
                .append("mockConfigId", getMockConfigId())
                .append("mockReqParams", getMockReqParams())
                .append("createdTime", getCreatedTime())
                .append("updatedTime", getUpdatedTime())
                .append("timeout", getTimeout())
                .toString();
    }
}
