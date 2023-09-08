package io.github.lancelothuxi.mock.controller;

import java.util.List;

import io.github.lancelothuxi.mock.common.annotation.Log;
import io.github.lancelothuxi.mock.common.core.controller.BaseController;
import io.github.lancelothuxi.mock.common.core.domain.AjaxResult;
import io.github.lancelothuxi.mock.common.core.page.TableDataInfo;
import io.github.lancelothuxi.mock.common.enums.BusinessType;
import io.github.lancelothuxi.mock.common.utils.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.lancelothuxi.mock.domain.KubeConfig;
import io.github.lancelothuxi.mock.service.IKubeConfigService;

import static io.github.lancelothuxi.mock.common.utils.PageUtils.startPage;

/**
 * k8s管理Controller
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
@Controller
@RequestMapping("/system/config")
public class KubeConfigController extends BaseController
{
    private String prefix = "system/config";

    @Autowired
    private IKubeConfigService kubeConfigService;

    @RequiresPermissions("system:config:view")
    @GetMapping()
    public String config()
    {
        return prefix + "/config";
    }

    /**
     * 查询k8s管理列表
     */
    @RequiresPermissions("system:config:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KubeConfig kubeConfig)
    {
        startPage();
        List<KubeConfig> list = kubeConfigService.selectKubeConfigList(kubeConfig);
        return getDataTable(list);
    }

    /**
     * 导出k8s管理列表
     */
    @RequiresPermissions("system:config:export")
    @Log(title = "k8s管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(KubeConfig kubeConfig)
    {
        List<KubeConfig> list = kubeConfigService.selectKubeConfigList(kubeConfig);
        ExcelUtil<KubeConfig> util = new ExcelUtil<KubeConfig>(KubeConfig.class);
        return util.exportExcel(list, "k8s管理数据");
    }

    /**
     * 新增k8s管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存k8s管理
     */
    @RequiresPermissions("system:config:add")
    @Log(title = "k8s管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(KubeConfig kubeConfig)
    {
        return toAjax(kubeConfigService.insertKubeConfig(kubeConfig));
    }

    /**
     * 修改k8s管理
     */
    @RequiresPermissions("system:config:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        KubeConfig kubeConfig = kubeConfigService.selectKubeConfigById(id);
        mmap.put("kubeConfig", kubeConfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存k8s管理
     */
    @RequiresPermissions("system:config:edit")
    @Log(title = "k8s管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(KubeConfig kubeConfig)
    {
        return toAjax(kubeConfigService.updateKubeConfig(kubeConfig));
    }

    /**
     * 删除k8s管理
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "k8s管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(kubeConfigService.deleteKubeConfigByIds(ids));
    }
}
