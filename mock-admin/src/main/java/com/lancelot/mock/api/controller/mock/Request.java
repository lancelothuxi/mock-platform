package com.lancelot.mock.api.controller.mock;

import com.lancelot.mock.api.controller.domain.MockConfig;

import java.util.ArrayList;
import java.util.List;

public class Request {

    private String appName;
    private List<MockConfig> mockConfigList =new ArrayList<>();

    private String type;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<MockConfig> getMockConfigList() {
        return mockConfigList;
    }

    public void setMockConfigList(List<MockConfig> mockConfigList) {
        this.mockConfigList = mockConfigList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
