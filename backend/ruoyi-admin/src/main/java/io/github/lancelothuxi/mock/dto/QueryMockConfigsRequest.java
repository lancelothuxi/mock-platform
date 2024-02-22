package io.github.lancelothuxi.mock.dto;


import io.github.lancelothuxi.mock.domain.MockConfig;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryMockConfigsRequest {

    private String applicationName;

    private String type;

    private List<MockConfig> mockConfigList = new ArrayList<>();

    private List<MockConfig> registerConfigList = new ArrayList<>();

}
