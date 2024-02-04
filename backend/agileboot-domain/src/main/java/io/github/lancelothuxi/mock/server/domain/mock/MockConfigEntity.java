package io.github.lancelothuxi.mock.server.domain.mock;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.lancelothuxi.mock.server.common.core.base.BaseEntity;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * mock配置
 * </p>
 *
 * @author lancelot
 * @since 2024-02-04
 */
@Getter
@Setter
@TableName("mock_config")
@ApiModel(value = "MockConfigEntity对象", description = "mock配置")
public class MockConfigEntity extends BaseEntity<MockConfigEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("应用名")
    @TableField("application_name")
    private String applicationName;

    @ApiModelProperty("服务名")
    @TableField("interface_name")
    private String interfaceName;

    @ApiModelProperty("方法名")
    @TableField("method_name")
    private String methodName;

    @ApiModelProperty("分组名")
    @TableField("group_name")
    private String groupName;

    @ApiModelProperty("版本号")
    @TableField("version")
    private String version;

    @ApiModelProperty("开关（1开启 0关闭）")
    @TableField("enabled")
    private String enabled;

    @ApiModelProperty("dubbo/dubboreset/feign")
    @TableField("`type`")
    private String type;

    @ApiModelProperty("创建时间")
    @TableField("created_time")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @TableField("updated_time")
    private Date updatedTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
