package io.github.lancelothuxi.mock.server.admin.controller.mock;


import io.github.lancelothuxi.mock.server.common.core.dto.ResponseDTO;
import io.github.lancelothuxi.mock.server.common.core.page.PageDTO;
import io.github.lancelothuxi.mock.server.domain.mock.MockConfigService;
import io.github.lancelothuxi.mock.server.domain.mock.dto.MockConfigDTO;
import io.github.lancelothuxi.mock.server.domain.mock.query.MockConfigQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import io.github.lancelothuxi.mock.server.common.core.base.BaseController;

/**
 * <p>
 * mock配置 前端控制器
 * </p>
 * @author lancelot
 * @since 2024-02-04
 */
@RestController
@RequestMapping("/mock/config")
@RequiredArgsConstructor
public class MockConfigController extends BaseController {

    private final MockConfigService mockConfigService;

    /**
     * 获取配置列表
     */
    @PreAuthorize("@permission.has('mock:config:list')")
    @GetMapping("/queryPage")
    public ResponseDTO<PageDTO<MockConfigDTO>> list(MockConfigQuery query) {
        PageDTO<MockConfigDTO> records = mockConfigService.getConfigList(query);
        return ResponseDTO.ok(records);
    }
}

