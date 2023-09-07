package io.github.lancelothuxi.mock.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.lancelothuxi.mock.common.annotation.Excel;
import io.github.lancelothuxi.mock.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务mock方法对象 mock_config
 *
 * @author lancelot huxisuz@gmail.com
 * @since 2023-05-11
 */
public class MockConfig extends BaseEntity {
  private static final long serialVersionUID = 1L;

  /** 主键 */
  private Long id;

  /** 服务名 */
  @Excel(name = "服务名")
  private String interfaceName;

  /** 方法名 */
  @Excel(name = "方法名")
  private String methodName;

  /** 分组名 */
  @Excel(name = "分组名")
  private String groupName;

  /** 版本号 */
  @Excel(name = "版本号")
  private String version;

  /** 参数数据值 */
  @Excel(name = "参数数据值")
  private String data;

  /** 开关（1开启 0关闭） */
  @Excel(name = "开关", readConverterExp = "1=开启,0=关闭")
  private String enabled;

  /** 创建时间 */
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
  private Date createdAt;

  /** 更新时间 */
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
  private Date updatedAt;

  /** 匹配参数数据的id */
  @Excel(name = "匹配参数数据的id")
  private Long matchDataId;

  /** 应用名 */
  @Excel(name = "应用名")
  private String appliactionName;

  /** 是否服务端mock 1是 0 表示客户端mock */
  @Excel(name = "是否服务端mock 1是 0 表示客户端mock")
  private Long serverSideMock;

  private String type;

  /** 是否是直接匹配，直接匹配的需要 吧mockdata数据 存放到dirMockDataMap 中 */
  private Integer directMatch;
  private List<MockData> mockDataList = new ArrayList<>();
  // 直接匹配，把mock数据的expectedValue 当做key, 可根据参数从map中直接获取
  private Map<String, MockData> dirMockDataMap = new HashMap<>();

  public Integer getDirectMatch() {
    return directMatch;
  }

  public void setDirectMatch(Integer directMatch) {
    this.directMatch = directMatch;
  }

  public Map<String, MockData> getDirMockDataMap() {
    return dirMockDataMap;
  }

  public Long getServerSideMock() {
    return serverSideMock;
  }

  public void setServerSideMock(Long serverSideMock) {
    this.serverSideMock = serverSideMock;
  }

  public List<MockData> getMockDataList() {
    return mockDataList;
  }

  public void setMockDataList(List<MockData> mockDataList) {
    this.mockDataList = mockDataList;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getEnabled() {
    return enabled;
  }

  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getMatchDataId() {
    return matchDataId;
  }

  public void setMatchDataId(Long matchDataId) {
    this.matchDataId = matchDataId;
  }

  public String getAppliactionName() {
    return appliactionName;
  }

  public void setAppliactionName(String appliactionName) {
    this.appliactionName = appliactionName;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        .append("id", getId())
        .append("interfaceName", getInterfaceName())
        .append("methodName", getMethodName())
        .append("groupName", getGroupName())
        .append("version", getVersion())
        .append("data", getData())
        .append("enabled", getEnabled())
        .append("createdAt", getCreatedAt())
        .append("updatedAt", getUpdatedAt())
        .append("matchDataId", getMatchDataId())
        .append("appliactionName", getAppliactionName())
        .toString();
  }
}
