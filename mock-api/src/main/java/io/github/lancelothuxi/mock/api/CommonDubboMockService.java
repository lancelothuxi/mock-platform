package io.github.lancelothuxi.mock.api;


/**
 * @author lancelot
 * @version 1.0
 * @since 2023/8/14 下午1:49
 */
public interface CommonDubboMockService {

    /**
     *
     * @param request mock request
     * @return MockResponse response that contains mock information
     */
    MockResponse doMockRequest(MockRequest request);
}
