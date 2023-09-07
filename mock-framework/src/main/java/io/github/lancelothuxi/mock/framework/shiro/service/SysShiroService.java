package io.github.lancelothuxi.mock.framework.shiro.service;

import io.github.lancelothuxi.mock.common.utils.StringUtils;
import io.github.lancelothuxi.mock.framework.shiro.session.OnlineSession;
import io.github.lancelothuxi.mock.system.domain.SysUserOnline;
import io.github.lancelothuxi.mock.system.service.ISysUserOnlineService;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 会话db操作处理
 *
 * @author lancelot huxisuz@gmail.com
 */
@Component
public class SysShiroService {
  @Autowired private ISysUserOnlineService onlineService;

  /**
   * 删除会话
   *
   * @param onlineSession 会话信息
   */
  public void deleteSession(OnlineSession onlineSession) {
    onlineService.deleteOnlineById(String.valueOf(onlineSession.getId()));
  }

  /**
   * 获取会话信息
   *
   * @param sessionId
   * @return
   */
  public Session getSession(Serializable sessionId) {
    SysUserOnline userOnline = onlineService.selectOnlineById(String.valueOf(sessionId));
    return StringUtils.isNull(userOnline) ? null : createSession(userOnline);
  }

  public Session createSession(SysUserOnline userOnline) {
    OnlineSession onlineSession = new OnlineSession();
    if (StringUtils.isNotNull(userOnline)) {
      onlineSession.setId(userOnline.getSessionId());
      onlineSession.setHost(userOnline.getIpaddr());
      onlineSession.setBrowser(userOnline.getBrowser());
      onlineSession.setOs(userOnline.getOs());
      onlineSession.setDeptName(userOnline.getDeptName());
      onlineSession.setLoginName(userOnline.getLoginName());
      onlineSession.setStartTimestamp(userOnline.getStartTimestamp());
      onlineSession.setLastAccessTime(userOnline.getLastAccessTime());
      onlineSession.setTimeout(userOnline.getExpireTime());
    }
    return onlineSession;
  }
}
