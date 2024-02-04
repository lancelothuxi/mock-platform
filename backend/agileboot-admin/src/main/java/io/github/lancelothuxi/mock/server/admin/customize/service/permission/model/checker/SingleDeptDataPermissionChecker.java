package io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.checker;

import io.github.lancelothuxi.mock.server.infrastructure.user.web.SystemLoginUser;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.AbstractDataPermissionChecker;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.DataCondition;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptService;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 数据权限测试接口
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleDeptDataPermissionChecker extends AbstractDataPermissionChecker {

    private SysDeptService deptService;

    @Override
    public boolean check(SystemLoginUser loginUser, DataCondition condition) {
        if (condition == null || loginUser == null) {
            return false;
        }

        if (loginUser.getDeptId() == null || condition.getTargetDeptId() == null) {
            return false;
        }

        Long currentDeptId = loginUser.getDeptId();
        Long targetDeptId = condition.getTargetDeptId();

        return Objects.equals(currentDeptId, targetDeptId);
    }


}
