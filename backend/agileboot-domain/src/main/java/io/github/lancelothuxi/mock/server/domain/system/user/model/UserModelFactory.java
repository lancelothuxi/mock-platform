package io.github.lancelothuxi.mock.server.domain.system.user.model;

import io.github.lancelothuxi.mock.server.common.exception.ApiException;
import io.github.lancelothuxi.mock.server.common.exception.error.ErrorCode;
import io.github.lancelothuxi.mock.server.domain.system.dept.model.DeptModelFactory;
import io.github.lancelothuxi.mock.server.domain.system.post.model.PostModelFactory;
import io.github.lancelothuxi.mock.server.domain.system.role.model.RoleModelFactory;
import io.github.lancelothuxi.mock.server.domain.system.user.db.SysUserEntity;
import io.github.lancelothuxi.mock.server.domain.system.user.db.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 用户模型工厂
 * @author valarchie
 */
@Component
@RequiredArgsConstructor
public class UserModelFactory {

    private final SysUserService userService;

    private final PostModelFactory postModelFactory;

    private final DeptModelFactory deptModelFactory;

    private final RoleModelFactory roleModelFactory;

    public UserModel loadById(Long userId) {
        SysUserEntity byId = userService.getById(userId);
        if (byId == null) {
            throw new ApiException(ErrorCode.Business.COMMON_OBJECT_NOT_FOUND, userId, "用户");
        }
        return new UserModel(byId, userService, postModelFactory, deptModelFactory, roleModelFactory);
    }

    public UserModel create() {
        return new UserModel(userService, postModelFactory, deptModelFactory, roleModelFactory);
    }

}
