package com.lancelot.mock.api.controller.polling.domain;

import com.lancelot.mock.api.controller.domain.MockConfig;

import java.util.List;

public class PollingParam {

    private boolean initial;
    private List<MockConfig> mockConfigs;

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public List<MockConfig> getMockConfigs() {
        return mockConfigs;
    }

    public void setMockConfigs(List<MockConfig> mockConfigs) {
        this.mockConfigs = mockConfigs;
    }
}
