package io.github.lancelothuxi.mock.polling.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * dubbo mock配置表 dubbo_mock_config
 *
 * @author luckyframe
 * @since 2022-02-11
 */
public class DubboMockConfig {
  private static final long serialVersionUID = 1L;

  /** */
  private Long id;
  /** 接口 */
  private String interfaceName;
  /** 方法 */
  private String methodName;
  /** 分组 */
  private String groupName;
  /** 版本 */
  private String version;
  /** mock数据 */
  private String data;
  /** 是否启用 */
  private Integer enabled;
  /** */
  private Date createTime;
  /** */
  private Date updateTime;

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

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Integer getEnabled() {
    return enabled;
  }

  public void setEnabled(Integer enabled) {
    this.enabled = enabled;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        .append("id", getId())
        .append("interfaceName", getInterfaceName())
        .append("methodName", getMethodName())
        .append("groupName", getGroupName())
        .append("version", getVersion())
        .append("data", getData())
        .append("enabled", getEnabled())
        .append("createTime", getCreateTime())
        .append("updateTime", getUpdateTime())
        .toString();
  }
}
