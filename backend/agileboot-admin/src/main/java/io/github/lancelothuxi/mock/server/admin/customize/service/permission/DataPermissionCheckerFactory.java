package io.github.lancelothuxi.mock.server.admin.customize.service.permission;

import cn.hutool.extra.spring.SpringUtil;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.AbstractDataPermissionChecker;
import io.github.lancelothuxi.mock.server.infrastructure.user.web.SystemLoginUser;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.checker.AllDataPermissionChecker;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.checker.CustomDataPermissionChecker;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.checker.DefaultDataPermissionChecker;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.checker.DeptTreeDataPermissionChecker;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.checker.OnlySelfDataPermissionChecker;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.checker.SingleDeptDataPermissionChecker;
import io.github.lancelothuxi.mock.server.infrastructure.user.web.DataScopeEnum;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptService;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

/**
 * 数据权限检测器工厂
 * @author valarchie
 */
@Component
public class DataPermissionCheckerFactory {
    private static AbstractDataPermissionChecker allChecker;
    private static AbstractDataPermissionChecker customChecker;
    private static AbstractDataPermissionChecker singleDeptChecker;
    private static AbstractDataPermissionChecker deptTreeChecker;
    private static AbstractDataPermissionChecker onlySelfChecker;
    private static AbstractDataPermissionChecker defaultSelfChecker;


    @PostConstruct
    public void initAllChecker() {
        SysDeptService deptService = SpringUtil.getBean(SysDeptService.class);

        allChecker = new AllDataPermissionChecker();
        customChecker = new CustomDataPermissionChecker(deptService);
        singleDeptChecker = new SingleDeptDataPermissionChecker(deptService);
        deptTreeChecker = new DeptTreeDataPermissionChecker(deptService);
        onlySelfChecker = new OnlySelfDataPermissionChecker(deptService);
        defaultSelfChecker = new DefaultDataPermissionChecker();
    }


    public static AbstractDataPermissionChecker getChecker(SystemLoginUser loginUser) {
        if (loginUser == null) {
            return deptTreeChecker;
        }

        DataScopeEnum dataScope = loginUser.getRoleInfo().getDataScope();
        switch (dataScope) {
            case ALL:
                return allChecker;
            case CUSTOM_DEFINE:
                return customChecker;
            case SINGLE_DEPT:
                return singleDeptChecker;
            case DEPT_TREE:
                return deptTreeChecker;
            case ONLY_SELF:
                return onlySelfChecker;
            default:
                return defaultSelfChecker;
        }
    }

}
