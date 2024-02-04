package io.github.lancelothuxi.mock.server.domain.system.monitor.dto;

import io.github.lancelothuxi.mock.server.domain.common.cache.CacheCenter;
import io.github.lancelothuxi.mock.server.infrastructure.user.web.SystemLoginUser;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptEntity;
import lombok.Data;

/**
 * 当前在线会话
 *
 * @author ruoyi
 */
@Data
public class OnlineUserDTO {

    /**
     * 会话编号
     */
    private String tokenId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 登录IP地址
     */
    private String ipAddress;

    /**
     * 登录地址
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String operationSystem;

    /**
     * 登录时间
     */
    private Long loginTime;


    public OnlineUserDTO(SystemLoginUser user) {
        if (user == null) {
            return;
        }
        this.setTokenId(user.getCachedKey());
        this.tokenId = user.getCachedKey();
        this.username = user.getUsername();
        this.ipAddress = user.getLoginInfo().getIpAddress();
        this.loginLocation = user.getLoginInfo().getLocation();
        this.browser = user.getLoginInfo().getBrowser();
        this.operationSystem = user.getLoginInfo().getOperationSystem();
        this.loginTime = user.getLoginInfo().getLoginTime();

        SysDeptEntity deptEntity = CacheCenter.deptCache.get(user.getDeptId() + "");

        if (deptEntity != null) {
            this.deptName = deptEntity.getDeptName();
        }
    }

}
