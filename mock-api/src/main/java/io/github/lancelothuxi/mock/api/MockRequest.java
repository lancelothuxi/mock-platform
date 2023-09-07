package io.github.lancelothuxi.mock.api;

import java.io.Serializable;

/**
 * @author lancelot
 * @version 1.0
 * @since 2023/8/14 下午1:49
 */
public class MockRequest implements Serializable {

  /** dubbo interface name* */
  private String interfaceName;

  /** dubbo method name* */
  private String methodName;

  /** dubbo group name* */
  private String groupName;

  /** dubbo version* */
  private String version;

  /** args* */
  private String args;

  /** appName* */
  private String appName;

  public MockRequest() {}

  public String getAppName() {
    return this.appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getInterfaceName() {
    return this.interfaceName;
  }

  public void setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
  }

  public String getMethodName() {
    return this.methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getGroupName() {
    return this.groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getVersion() {
    return this.version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getArgs() {
    return this.args;
  }

  public void setArgs(String args) {
    this.args = args;
  }
}
