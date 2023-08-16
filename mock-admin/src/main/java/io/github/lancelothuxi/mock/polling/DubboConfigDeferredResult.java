package io.github.lancelothuxi.mock.polling;

import io.github.lancelothuxi.mock.domain.MockConfig;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class DubboConfigDeferredResult extends DeferredResult {

    /**客户端关心的*/
    private List<MockConfig> subscribedConfigs=new CopyOnWriteArrayList<>();


    public DubboConfigDeferredResult() {
    }


    public DubboConfigDeferredResult(Long timeout, List<MockConfig> mockConfigs) {
        super(timeout);
        subscribedConfigs.addAll(mockConfigs);
    }


    public DubboConfigDeferredResult(Long timeout, Supplier<?> timeoutResult,List<MockConfig> mockConfigs) {
        super(timeout,timeoutResult);
        subscribedConfigs.addAll(mockConfigs);
    }



    public DubboConfigDeferredResult(Long timeout, List<MockConfig> mockConfigs,Runnable timeoutCallback) {
        this(timeout,mockConfigs);
        this.onTimeout(timeoutCallback);
    }

    public DubboConfigDeferredResult(Long timeout, Supplier<?> timeoutResult,List<MockConfig> mockConfigs,Runnable timeoutCallback) {

        super(timeout, timeoutResult);
        this.onTimeout(timeoutCallback);
        subscribedConfigs.addAll(mockConfigs);

    }


    public DubboConfigDeferredResult(Long timeout, Supplier<?> timeoutResult, MockConfig mockConfig) {
        super(timeout, timeoutResult);
        subscribedConfigs.add(mockConfig);
    }

    public DubboConfigDeferredResult(Long timeout, Supplier<?> timeoutResult, MockConfig mockConfig,Runnable timeoutCallback) {
        this(timeout, timeoutResult,mockConfig);
        this.onTimeout(timeoutCallback);
    }


    public List<MockConfig> getSubscribedConfigs() {
        return subscribedConfigs;
    }

    public void setSubscribedConfigs(List<MockConfig> subscribedConfigs) {
        this.subscribedConfigs = subscribedConfigs;
    }


    public boolean setResult(Object result,Runnable runnable) {
        runnable.run();
        return super.setResult(result);
    }
}
