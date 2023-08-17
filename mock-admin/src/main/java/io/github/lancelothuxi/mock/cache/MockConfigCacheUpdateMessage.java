package io.github.lancelothuxi.mock.cache;

import java.io.Serializable;

/**
 * @author lancelot
 * @version 1.0
 * @since 2023/7/18 下午3:25
 */
public class MockConfigCacheUpdateMessage implements Serializable {

    /** 主键 */
    private Long id;

    /** 服务名 */
    private String interfaceName;

    /** 方法名 */
    private String methodName;

    /** 分组名 */
    private String groupName;

    /** 版本号 */
    private String version;

    private String appliactionName;

    private String type;

    private String op;

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppliactionName() {
        return appliactionName;
    }

    public void setAppliactionName(String appliactionName) {
        this.appliactionName = appliactionName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
