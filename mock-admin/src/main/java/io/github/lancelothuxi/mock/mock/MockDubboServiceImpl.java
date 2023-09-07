package io.github.lancelothuxi.mock.mock;

import io.github.lancelothuxi.mock.api.CommonDubboMockService;
import io.github.lancelothuxi.mock.api.MockRequest;
import io.github.lancelothuxi.mock.api.MockResponse;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


@DubboService(timeout = 5000)
public class MockDubboServiceImpl implements CommonDubboMockService {

    @Autowired
    private CommonMockService commonMockService;

    @Override
    public MockResponse doMockRequest(MockRequest request) {
        return commonMockService.doMockRequest(request);
    }
}
