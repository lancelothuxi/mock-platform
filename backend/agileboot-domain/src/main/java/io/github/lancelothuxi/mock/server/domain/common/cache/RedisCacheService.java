package io.github.lancelothuxi.mock.server.domain.common.cache;

import cn.hutool.extra.spring.SpringUtil;
import io.github.lancelothuxi.mock.server.infrastructure.cache.RedisUtil;
import io.github.lancelothuxi.mock.server.infrastructure.cache.redis.CacheKeyEnum;
import io.github.lancelothuxi.mock.server.infrastructure.cache.redis.RedisCacheTemplate;
import io.github.lancelothuxi.mock.server.infrastructure.user.web.SystemLoginUser;
import io.github.lancelothuxi.mock.server.domain.system.post.db.SysPostEntity;
import io.github.lancelothuxi.mock.server.domain.system.role.db.SysRoleEntity;
import io.github.lancelothuxi.mock.server.domain.system.user.db.SysUserEntity;
import io.github.lancelothuxi.mock.server.domain.system.post.db.SysPostService;
import io.github.lancelothuxi.mock.server.domain.system.role.db.SysRoleService;
import io.github.lancelothuxi.mock.server.domain.system.user.db.SysUserService;
import java.io.Serializable;
import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author valarchie
 */
@Component
@RequiredArgsConstructor
public class RedisCacheService {

    private final RedisUtil redisUtil;

    public RedisCacheTemplate<String> captchaCache;
    public RedisCacheTemplate<SystemLoginUser> loginUserCache;
    public RedisCacheTemplate<SysUserEntity> userCache;
    public RedisCacheTemplate<SysRoleEntity> roleCache;

    public RedisCacheTemplate<SysPostEntity> postCache;

//    public RedisCacheTemplate<RoleInfo> roleModelInfoCache;

    @PostConstruct
    public void init() {

        captchaCache = new RedisCacheTemplate<>(redisUtil, CacheKeyEnum.CAPTCHAT);

        loginUserCache = new RedisCacheTemplate<>(redisUtil, CacheKeyEnum.LOGIN_USER_KEY);

        userCache = new RedisCacheTemplate<SysUserEntity>(redisUtil, CacheKeyEnum.USER_ENTITY_KEY) {
            @Override
            public SysUserEntity getObjectFromDb(Object id) {
                SysUserService userService = SpringUtil.getBean(SysUserService.class);
                return userService.getById((Serializable) id);
            }
        };

        roleCache = new RedisCacheTemplate<SysRoleEntity>(redisUtil, CacheKeyEnum.ROLE_ENTITY_KEY) {
            @Override
            public SysRoleEntity getObjectFromDb(Object id) {
                SysRoleService roleService = SpringUtil.getBean(SysRoleService.class);
                return roleService.getById((Serializable) id);
            }
        };

//        roleModelInfoCache = new RedisCacheTemplate<RoleInfo>(redisUtil, CacheKeyEnum.ROLE_MODEL_INFO_KEY) {
//            @Override
//            public RoleInfo getObjectFromDb(Object id) {
//                UserDetailsService userDetailsService = SpringUtil.getBean(UserDetailsService.class);
//                return userDetailsService.getRoleInfo((Long) id);
//            }
//
//        };

        postCache = new RedisCacheTemplate<SysPostEntity>(redisUtil, CacheKeyEnum.POST_ENTITY_KEY) {
            @Override
            public SysPostEntity getObjectFromDb(Object id) {
                SysPostService postService = SpringUtil.getBean(SysPostService.class);
                return postService.getById((Serializable) id);
            }

        };


    }


}
