package io.github.lancelothuxi.mock.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * mock配置对象 mock_config
 * 
 * @author huxisuz@gmail.com
 * @date 2024-02-06
 */
public class MockConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 应用名 */
    @Excel(name = "应用名")
    private String applicationName;

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

    /** 开关（1开启 0关闭） */
    @Excel(name = "开关", readConverterExp = "1=开启,0=关闭")
    private String enabled;

    /** dubbo/dubboreset/feign */
    private String type;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setApplicationName(String applicationName) 
    {
        this.applicationName = applicationName;
    }

    public String getApplicationName() 
    {
        return applicationName;
    }
    public void setInterfaceName(String interfaceName) 
    {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceName() 
    {
        return interfaceName;
    }
    public void setMethodName(String methodName) 
    {
        this.methodName = methodName;
    }

    public String getMethodName() 
    {
        return methodName;
    }
    public void setGroupName(String groupName) 
    {
        this.groupName = groupName;
    }

    public String getGroupName() 
    {
        return groupName;
    }
    public void setVersion(String version) 
    {
        this.version = version;
    }

    public String getVersion() 
    {
        return version;
    }
    public void setEnabled(String enabled) 
    {
        this.enabled = enabled;
    }

    public String getEnabled() 
    {
        return enabled;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }
    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("applicationName", getApplicationName())
            .append("interfaceName", getInterfaceName())
            .append("methodName", getMethodName())
            .append("groupName", getGroupName())
            .append("version", getVersion())
            .append("enabled", getEnabled())
            .append("type", getType())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
