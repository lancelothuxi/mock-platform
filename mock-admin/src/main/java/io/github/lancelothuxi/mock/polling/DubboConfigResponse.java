package io.github.lancelothuxi.mock.polling;


import io.github.lancelothuxi.mock.domain.MockConfig;

import java.io.Serializable;

public class DubboConfigResponse implements Serializable {

    MockConfig dubboMockConfig;

    /**
     * add / delete / update /
     */
    private OpEnum op;


    public DubboConfigResponse(MockConfig dubboMockConfig, OpEnum op) {
        this.dubboMockConfig = dubboMockConfig;
        this.op = op;
    }


    public MockConfig getDubboMockConfig() {
        return dubboMockConfig;
    }

    public void setDubboMockConfig(MockConfig dubboMockConfig) {
        this.dubboMockConfig = dubboMockConfig;
    }

    public OpEnum getOp() {
        return op;
    }

    public void setOp(OpEnum op) {
        this.op = op;
    }

    public static enum  OpEnum{

        ADD("add"), DELETE("delete"), UPDATE("update");

        String key;

        OpEnum(String key) {
            this.key = key;
        }
    }

}
