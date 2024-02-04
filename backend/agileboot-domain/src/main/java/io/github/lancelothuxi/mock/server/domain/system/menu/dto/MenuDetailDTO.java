package io.github.lancelothuxi.mock.server.domain.system.menu.dto;

import cn.hutool.core.util.StrUtil;
import io.github.lancelothuxi.mock.server.common.utils.jackson.JacksonUtil;
import io.github.lancelothuxi.mock.server.domain.system.menu.db.SysMenuEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuDetailDTO extends MenuDTO {

    public MenuDetailDTO(SysMenuEntity entity) {
        super(entity);
        if (entity == null) {
            return;
        }
        if (StrUtil.isNotEmpty(entity.getMetaInfo()) && JacksonUtil.isJson(entity.getMetaInfo())) {
            this.meta = JacksonUtil.from(entity.getMetaInfo(), MetaDTO.class);
        }
        this.permission = entity.getPermission();
    }

    private String permission;
    private MetaDTO meta;

}
