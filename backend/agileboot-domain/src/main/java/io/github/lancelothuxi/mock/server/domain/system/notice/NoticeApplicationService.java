package io.github.lancelothuxi.mock.server.domain.system.notice;

import io.github.lancelothuxi.mock.server.common.core.page.PageDTO;
import io.github.lancelothuxi.mock.server.domain.common.command.BulkOperationCommand;
import io.github.lancelothuxi.mock.server.domain.system.notice.command.NoticeAddCommand;
import io.github.lancelothuxi.mock.server.domain.system.notice.command.NoticeUpdateCommand;
import io.github.lancelothuxi.mock.server.domain.system.notice.dto.NoticeDTO;
import io.github.lancelothuxi.mock.server.domain.system.notice.model.NoticeModel;
import io.github.lancelothuxi.mock.server.domain.system.notice.model.NoticeModelFactory;
import io.github.lancelothuxi.mock.server.domain.system.notice.query.NoticeQuery;
import io.github.lancelothuxi.mock.server.domain.system.notice.db.SysNoticeEntity;
import io.github.lancelothuxi.mock.server.domain.system.notice.db.SysNoticeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author valarchie
 */
@Service
@RequiredArgsConstructor
public class NoticeApplicationService {

    private final SysNoticeService noticeService;

    private final NoticeModelFactory noticeModelFactory;

    public PageDTO<NoticeDTO> getNoticeList(NoticeQuery query) {
        Page<SysNoticeEntity> page = noticeService.getNoticeList(query.toPage(), query.toQueryWrapper());
        List<NoticeDTO> records = page.getRecords().stream().map(NoticeDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }


    public NoticeDTO getNoticeInfo(Long id) {
        NoticeModel noticeModel = noticeModelFactory.loadById(id);
        return new NoticeDTO(noticeModel);
    }


    public void addNotice(NoticeAddCommand addCommand) {
        NoticeModel noticeModel = noticeModelFactory.create();
        noticeModel.loadAddCommand(addCommand);

        noticeModel.checkFields();

        noticeModel.insert();
    }


    public void updateNotice(NoticeUpdateCommand updateCommand) {
        NoticeModel noticeModel = noticeModelFactory.loadById(updateCommand.getNoticeId());
        noticeModel.loadUpdateCommand(updateCommand);

        noticeModel.checkFields();

        noticeModel.updateById();
    }

    public void deleteNotice(BulkOperationCommand<Integer> deleteCommand) {
        noticeService.removeBatchByIds(deleteCommand.getIds());
    }




}