package com.lancelot.mock.vo;

import java.util.ArrayList;
import java.util.List;

public class MockConfigVo {

    /** 服务名 */
    private String interfaceName;

    /** 方法名 */
    private String methodName;

    /** 分组名 */
    private String groupName;

    /** 版本号 */
    private String version;

    private Long serverSideMock;

    private String appliactionName;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private List<MockDataVo> mockDataList =new ArrayList<>();

    public String getAppliactionName() {
        return appliactionName;
    }

    public void setAppliactionName(String appliactionName) {
        this.appliactionName = appliactionName;
    }

    public Long getServerSideMock() {
        return serverSideMock;
    }

    public void setServerSideMock(Long serverSideMock) {
        this.serverSideMock = serverSideMock;
    }

    public List<MockDataVo> getMockDataList() {
        return mockDataList;
    }

    public void setMockDataList(List<MockDataVo> mockDataList) {
        this.mockDataList = mockDataList;
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
}
