package com.lancelot.mock.api.controller.vo;

/**
 * mock配置关联响应数据对象 mock_data
 *
 * @author ruoyi
 * @date 2023-05-10
 */
public class MockDataVo
{

    /** mock响应数据值 */
    private String data;

    private String mockReqParams;

    private Integer timeout;

    private String expectedValue;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMockReqParams() {
        return mockReqParams;
    }

    public void setMockReqParams(String mockReqParams) {
        this.mockReqParams = mockReqParams;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }
}
