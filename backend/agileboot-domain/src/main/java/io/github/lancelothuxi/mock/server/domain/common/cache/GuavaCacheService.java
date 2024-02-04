package io.github.lancelothuxi.mock.server.domain.common.cache;


import io.github.lancelothuxi.mock.server.infrastructure.cache.guava.AbstractGuavaCacheTemplate;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptEntity;
import io.github.lancelothuxi.mock.server.domain.system.config.db.SysConfigService;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author valarchie
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class GuavaCacheService {

    private final SysConfigService configService;

    private final SysDeptService deptService;

    public final AbstractGuavaCacheTemplate<String> configCache = new AbstractGuavaCacheTemplate<String>() {
        @Override
        public String getObjectFromDb(Object id) {
            return configService.getConfigValueByKey(id.toString());
        }
    };

    public final AbstractGuavaCacheTemplate<SysDeptEntity> deptCache = new AbstractGuavaCacheTemplate<SysDeptEntity>() {
        @Override
        public SysDeptEntity getObjectFromDb(Object id) {
            return deptService.getById(id.toString());
        }
    };


}
