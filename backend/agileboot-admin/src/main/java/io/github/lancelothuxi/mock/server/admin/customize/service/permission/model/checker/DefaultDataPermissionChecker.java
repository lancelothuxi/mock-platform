package io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.checker;

import io.github.lancelothuxi.mock.server.infrastructure.user.web.SystemLoginUser;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.AbstractDataPermissionChecker;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.DataCondition;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据权限测试接口
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultDataPermissionChecker extends AbstractDataPermissionChecker {

    private SysDeptService deptService;

    @Override
    public boolean check(SystemLoginUser loginUser, DataCondition condition) {
        return false;
    }

}
