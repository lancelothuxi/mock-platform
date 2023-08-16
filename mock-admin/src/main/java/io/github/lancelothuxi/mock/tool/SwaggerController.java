package io.github.lancelothuxi.mock.tool;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.github.lancelothuxi.mock.common.core.controller.BaseController;

/**
 * swagger 接口
 * 
 * @author lancelot huxisuz@gmail.com
 */
@Controller
@RequestMapping("/tool/swagger")
public class SwaggerController extends BaseController
{
    @RequiresPermissions("tool:swagger:view")
    @GetMapping()
    public String index()
    {
        return redirect("/swagger-ui/index.html");
    }
}
