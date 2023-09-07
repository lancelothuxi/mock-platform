package io.github.lancelothuxi.mock.tool;

import io.github.lancelothuxi.mock.common.core.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * build 表单构建
 *
 * @author lancelot huxisuz@gmail.com
 */
@Controller
@RequestMapping("/tool/build")
public class BuildController extends BaseController {
  private String prefix = "tool/build";

  @RequiresPermissions("tool:build:view")
  @GetMapping()
  public String build() {
    return prefix + "/build";
  }
}
