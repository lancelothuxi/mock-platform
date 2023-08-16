package io.github.lancelothuxi.mock.api;

import java.io.Serializable;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/14 下午1:49
 */
public class MockResponse implements Serializable {
    private String data;
    private int code;

    public MockResponse() {
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean success() {
        return this.code == 0;
    }
}
