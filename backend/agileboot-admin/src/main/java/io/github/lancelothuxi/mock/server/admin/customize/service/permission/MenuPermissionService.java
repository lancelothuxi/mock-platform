package io.github.lancelothuxi.mock.server.admin.customize.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.github.lancelothuxi.mock.server.infrastructure.user.AuthenticationUtils;
import io.github.lancelothuxi.mock.server.infrastructure.user.web.SystemLoginUser;
import io.github.lancelothuxi.mock.server.infrastructure.user.web.RoleInfo;
import java.util.Set;

import org.springframework.stereotype.Service;

/**
 *
 * @author valarchie
 */
@Service("permission")
public class MenuPermissionService {


    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean has(String permission) {
        if (StrUtil.isEmpty(permission)) {
            return false;
        }
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        if (loginUser == null || CollUtil.isEmpty(loginUser.getRoleInfo().getMenuPermissions())) {
            return false;
        }
        return has(loginUser.getRoleInfo().getMenuPermissions(), permission);
    }


    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    private boolean has(Set<String> permissions, String permission) {
        return permissions.contains(RoleInfo.ALL_PERMISSIONS) || permissions.contains(StrUtil.trim(permission));
    }

}
