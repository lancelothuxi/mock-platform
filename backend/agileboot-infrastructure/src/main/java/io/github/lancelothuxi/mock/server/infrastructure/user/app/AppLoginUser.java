package io.github.lancelothuxi.mock.server.infrastructure.user.app;

import io.github.lancelothuxi.mock.server.infrastructure.user.base.BaseLoginUser;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录用户身份权限
 * @author valarchie
 */
@Data
@NoArgsConstructor
public class AppLoginUser extends BaseLoginUser {

    private static final long serialVersionUID = 1L;

    private boolean isVip;


    public AppLoginUser(Long userId, Boolean isVip, String cachedKey) {
        this.cachedKey = cachedKey;
        this.userId = userId;
        this.isVip = isVip;
    }


}
