package io.github.lancelothuxi.mock.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * mock配置对象 mock_config
 *
 * @author huxisuz@gmail.com
 * @date 2024-02-06
 */
@Data
public class MockConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 应用名
     */
    @Excel(name = "应用名")
    private String applicationName;

    /**
     * 服务名
     */
    @Excel(name = "服务名")
    private String interfaceName;

    /**
     * 方法名
     */
    @Excel(name = "方法名")
    private String methodName;

    /**
     * 分组名
     */
    @Excel(name = "分组名")
    private String groupName;

    /**
     * 版本号
     */
    @Excel(name = "版本号")
    private String version;

    /**
     * 开关（1开启 0关闭）
     */
    @Excel(name = "开关", readConverterExp = "1=开启,0=关闭")
    private String enabled;

    /**
     * dubbo/dubboreset/feign
     */
    private String type;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public boolean configEnabled(){
        return "1".equals(enabled);
    }
}
