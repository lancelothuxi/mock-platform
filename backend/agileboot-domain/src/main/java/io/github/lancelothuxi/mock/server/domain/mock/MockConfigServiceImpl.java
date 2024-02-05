package io.github.lancelothuxi.mock.server.domain.mock;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.lancelothuxi.mock.server.common.core.page.PageDTO;
import io.github.lancelothuxi.mock.server.domain.mock.convert.MockConfigConverter;
import io.github.lancelothuxi.mock.server.domain.mock.dto.MockConfigDTO;
import io.github.lancelothuxi.mock.server.domain.mock.query.MockConfigQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * mock配置 服务实现类
 * </p>
 *
 * @author lancelot
 * @since 2024-02-04
 */
@Service
public class MockConfigServiceImpl extends ServiceImpl<MockConfigMapper, MockConfigEntity> implements MockConfigService {

    @Override
    public PageDTO<MockConfigDTO> getConfigList(MockConfigQuery query) {
        Page<MockConfigEntity> rowPage = new Page(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<MockConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
        Page<MockConfigEntity> records = this.baseMapper.selectPage(rowPage, queryWrapper);
        List<MockConfigDTO> mockConfigDTOS = MockConfigConverter.INSTANCE.toDto(records.getRecords());
        return new PageDTO<>(mockConfigDTOS, records.getTotal());
    }
}
