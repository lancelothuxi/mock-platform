package io.github.lancelothuxi.mock.mock;

import io.github.lancelothuxi.mock.vo.MockConfigVo;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private List<MockConfigVo> mockConfigs =new ArrayList<>();

    public List<MockConfigVo> getMockConfigs() {
        return mockConfigs;
    }

    public void setMockConfigs(List<MockConfigVo> mockConfigs) {
        this.mockConfigs = mockConfigs;
    }
}