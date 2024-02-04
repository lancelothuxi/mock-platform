package io.github.lancelothuxi.mock.server.domain.system.notice.model;

import cn.hutool.core.bean.BeanUtil;
import io.github.lancelothuxi.mock.server.domain.system.notice.command.NoticeAddCommand;
import io.github.lancelothuxi.mock.server.domain.system.notice.command.NoticeUpdateCommand;
import io.github.lancelothuxi.mock.server.common.enums.common.NoticeTypeEnum;
import io.github.lancelothuxi.mock.server.common.enums.common.StatusEnum;
import io.github.lancelothuxi.mock.server.common.enums.BasicEnumUtil;
import io.github.lancelothuxi.mock.server.domain.system.notice.db.SysNoticeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class NoticeModel extends SysNoticeEntity {

    public NoticeModel(SysNoticeEntity entity) {
        if (entity != null) {
            BeanUtil.copyProperties(entity, this);
        }
    }

    public void loadAddCommand(NoticeAddCommand command) {
        if (command != null) {
            BeanUtil.copyProperties(command, this, "noticeId");
        }
    }

    public void loadUpdateCommand(NoticeUpdateCommand command) {
        if (command != null) {
            loadAddCommand(command);
        }
    }

    public void checkFields() {
        BasicEnumUtil.fromValue(NoticeTypeEnum.class, getNoticeType());
        BasicEnumUtil.fromValue(StatusEnum.class, getStatus());
    }

}
