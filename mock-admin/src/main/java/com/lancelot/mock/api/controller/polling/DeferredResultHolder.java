package com.lancelot.mock.api.controller.polling;

import com.lancelot.mock.api.controller.domain.MockConfig;
import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *wrapper for long polling deferredResult holder
 */
public class DeferredResultHolder {

    private Queue<DubboConfigDeferredResult> deferredResults = new ConcurrentLinkedQueue<>();

    public void add(DubboConfigDeferredResult deferredResult) {
        this.deferredResults.add(deferredResult);
    }

    public boolean remove(DubboConfigDeferredResult deferredResult){
        return this.deferredResults.remove(deferredResult);
    }

    /**
     * handle mockConfigChange and release held deferred request
     * @param dubboMockConfig
     */
    public void handleMockConfigChange(MockConfig dubboMockConfig, DubboConfigResponse.OpEnum op) {

        final Iterator<DubboConfigDeferredResult> iterator = deferredResults.iterator();

        while (iterator.hasNext()) {

            DubboConfigDeferredResult currentDef = iterator.next();

            final List<MockConfig> subscribedConfigs = currentDef.getSubscribedConfigs();

            if (CollectionUtils.isEmpty(subscribedConfigs)) {
                continue;
            }

            final boolean contains = contains(subscribedConfigs, dubboMockConfig);

            if(!contains){
                continue;
            }

            currentDef.setResult(Arrays.asList(new DubboConfigResponse(dubboMockConfig,op)));
            iterator.remove();
        }
    }



    private boolean contains(List<MockConfig> subscribedConfigs, MockConfig dubboMockConfig) {

        for (MockConfig subscribedConfig : subscribedConfigs) {

            //equals
            if (
                    stringEqualsIgnoreNull(subscribedConfig.getInterfaceName(), dubboMockConfig.getInterfaceName()) &&
                            stringEqualsIgnoreNull(subscribedConfig.getMethodName(), dubboMockConfig.getMethodName()) &&
                            stringEqualsIgnoreNull(subscribedConfig.getGroupName(), dubboMockConfig.getGroupName()) &&
                            stringEqualsIgnoreNull(subscribedConfig.getVersion(), dubboMockConfig.getVersion())) {

                return true;
            }
        }

        return false;
    }


    private boolean stringEqualsIgnoreNull(String a, String b) {

        if (a == null) {
            a = "";
        }

        if (b == null) {
            b = "";
        }

        return a.equals(b);
    }
}
