package io.github.lancelothuxi.mock.server.admin.customize.service.permission.model;

import io.github.lancelothuxi.mock.server.infrastructure.user.web.SystemLoginUser;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptService;
import lombok.Data;

/**
 * 数据权限测试接口
 * @author valarchie
 */
@Data
public abstract class AbstractDataPermissionChecker {

    private SysDeptService deptService;

    /**
     * 检测当前用户对于 给定条件的数据 是否有权限
     *
     * @param loginUser 登录用户
     * @param condition 条件
     * @return 校验结果
     */
    public abstract boolean check(SystemLoginUser loginUser, DataCondition condition);

}
