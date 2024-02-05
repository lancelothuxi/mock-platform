package io.github.lancelothuxi.mock.server.domain.mock.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.lancelothuxi.mock.server.common.core.page.AbstractPageQuery;
import io.github.lancelothuxi.mock.server.domain.system.config.db.SysConfigEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Schema(name = "配置查询参数")
public class MockConfigQuery extends AbstractPageQuery<SysConfigEntity> {

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "配置key")
    private String configKey;

    @Schema(description = "是否允许更改配置")
    private Boolean isAllowChange;


    @Override
    public QueryWrapper<SysConfigEntity> addQueryCondition() {
        QueryWrapper<SysConfigEntity> queryWrapper = new QueryWrapper<SysConfigEntity>()
            .like(StrUtil.isNotEmpty(configName), "config_name", configName)
            .eq(StrUtil.isNotEmpty(configKey), "config_key", configKey)
            .eq(isAllowChange != null, "is_allow_change", isAllowChange);

        this.timeRangeColumn = "create_time";

        return queryWrapper;
    }
}
