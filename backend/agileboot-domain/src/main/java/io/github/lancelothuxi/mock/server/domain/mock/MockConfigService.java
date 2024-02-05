package io.github.lancelothuxi.mock.server.domain.mock;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.lancelothuxi.mock.server.common.core.page.PageDTO;
import io.github.lancelothuxi.mock.server.domain.mock.dto.MockConfigDTO;
import io.github.lancelothuxi.mock.server.domain.mock.query.MockConfigQuery;
import io.github.lancelothuxi.mock.server.domain.system.config.db.SysConfigEntity;
import io.github.lancelothuxi.mock.server.domain.system.config.dto.ConfigDTO;
import io.github.lancelothuxi.mock.server.domain.system.config.query.ConfigQuery;
import io.github.lancelothuxi.mock.server.domain.system.post.db.SysPostEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * mock配置 服务类
 * </p>
 *
 * @author lancelot
 * @since 2024-02-04
 */
public interface MockConfigService extends IService<MockConfigEntity> {

    public PageDTO<MockConfigDTO> getConfigList(MockConfigQuery query);
}
