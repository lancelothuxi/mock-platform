package io.github.lancelothuxi.mock.server.domain.system.notice.model;

import io.github.lancelothuxi.mock.server.common.exception.ApiException;
import io.github.lancelothuxi.mock.server.common.exception.error.ErrorCode;
import io.github.lancelothuxi.mock.server.domain.system.notice.db.SysNoticeEntity;
import io.github.lancelothuxi.mock.server.domain.system.notice.db.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 公告模型工厂
 * @author valarchie
 */
@Component
@RequiredArgsConstructor
public class NoticeModelFactory {

    private final SysNoticeService noticeService;

    public NoticeModel loadById(Long noticeId) {
        SysNoticeEntity byId = noticeService.getById(noticeId);

        if (byId == null) {
            throw new ApiException(ErrorCode.Business.COMMON_OBJECT_NOT_FOUND, noticeId, "通知公告");
        }

        return new NoticeModel(byId);
    }

    public NoticeModel create() {
        return new NoticeModel();
    }


}
