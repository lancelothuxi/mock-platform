package io.github.lancelothuxi.mock.server.domain.system.notice.dto;

import io.github.lancelothuxi.mock.server.domain.common.cache.CacheCenter;
import io.github.lancelothuxi.mock.server.domain.system.notice.db.SysNoticeEntity;
import io.github.lancelothuxi.mock.server.domain.system.user.db.SysUserEntity;
import java.util.Date;

import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class NoticeDTO {

    public NoticeDTO(SysNoticeEntity entity) {
        if (entity != null) {
            this.noticeId = entity.getNoticeId() + "";
            this.noticeTitle = entity.getNoticeTitle();
            this.noticeType = entity.getNoticeType();
            this.noticeContent = entity.getNoticeContent();
            this.status = entity.getStatus();
            this.createTime = entity.getCreateTime();

            SysUserEntity cacheUser = CacheCenter.userCache.getObjectById(entity.getCreatorId());
            if (cacheUser != null) {
                this.creatorName = cacheUser.getUsername();
            }
        }
    }

    private String noticeId;

    private String noticeTitle;

    private Integer noticeType;

    private String noticeContent;

    private Integer status;

    private Date createTime;

    private String creatorName;

}
