package io.github.lancelothuxi.mock.framework.aspectj;

import io.github.lancelothuxi.mock.common.core.context.PermissionContextHolder;
import io.github.lancelothuxi.mock.common.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 自定义权限拦截器，将权限字符串放到当前请求中以便用于多个角色匹配符合要求的权限
 *
 * @author lancelot huxisuz@gmail.com
 */
@Aspect
@Component
public class PermissionsAspect {
  @Before("@annotation(controllerRequiresPermissions)")
  public void doBefore(JoinPoint point, RequiresPermissions controllerRequiresPermissions)
      throws Throwable {
    handleRequiresPermissions(point, controllerRequiresPermissions);
  }

  protected void handleRequiresPermissions(
      final JoinPoint joinPoint, RequiresPermissions requiresPermissions) {
    PermissionContextHolder.setContext(StringUtils.join(requiresPermissions.value(), ","));
  }
}
