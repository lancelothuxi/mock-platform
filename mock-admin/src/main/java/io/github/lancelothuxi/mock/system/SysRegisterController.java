package io.github.lancelothuxi.mock.system;

import io.github.lancelothuxi.mock.common.core.domain.AjaxResult;
import io.github.lancelothuxi.mock.common.core.domain.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.lancelothuxi.mock.common.core.controller.BaseController;
import io.github.lancelothuxi.mock.common.utils.StringUtils;
import io.github.lancelothuxi.mock.framework.shiro.service.SysRegisterService;
import io.github.lancelothuxi.mock.system.service.ISysConfigService;

/**
 * 注册验证
 * 
 * @author lancelot huxisuz@gmail.com
 */
@Controller
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @GetMapping("/register")
    public String register()
    {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public AjaxResult ajaxRegister(SysUser user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
