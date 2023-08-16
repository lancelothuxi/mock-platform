package io.github.lancelothuxi.mock.mock;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/6/17 下午12:35
 */
public class MockExpression {

    private String jsonPath="";

    private String expectedValue="";


    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }
}
