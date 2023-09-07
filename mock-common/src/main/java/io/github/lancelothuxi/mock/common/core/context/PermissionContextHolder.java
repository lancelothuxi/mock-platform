package io.github.lancelothuxi.mock.common.core.context;

import io.github.lancelothuxi.mock.common.core.text.Convert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 权限信息
 *
 * @author lancelot huxisuz@gmail.com
 */
public class PermissionContextHolder {
  private static final String PERMISSION_CONTEXT_ATTRIBUTES = "PERMISSION_CONTEXT";

  public static String getContext() {
    return Convert.toStr(
        RequestContextHolder.currentRequestAttributes()
            .getAttribute(PERMISSION_CONTEXT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST));
  }

  public static void setContext(String permission) {
    RequestContextHolder.currentRequestAttributes()
        .setAttribute(PERMISSION_CONTEXT_ATTRIBUTES, permission, RequestAttributes.SCOPE_REQUEST);
  }
}
