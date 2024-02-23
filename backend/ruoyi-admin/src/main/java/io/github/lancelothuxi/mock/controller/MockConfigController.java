package io.github.lancelothuxi.mock.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.github.lancelothuxi.mock.dto.QueryMockConfigsRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import io.github.lancelothuxi.mock.domain.MockConfig;
import io.github.lancelothuxi.mock.service.IMockConfigService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * mock配置Controller
 *
 * @author huxisuz@gmail.com
 * @date 2024-02-06
 */
@RestController
@RequestMapping("/mock/config")
public class MockConfigController extends BaseController {
    @Autowired
    private IMockConfigService mockConfigService;

    /**
     * 查询mock配置列表
     */
    @PreAuthorize("@ss.hasPermi('mock:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(MockConfig mockConfig) {
        startPage();
        List<MockConfig> list = mockConfigService.query4Page(mockConfig);
        return getDataTable(list);
    }

    /**
     * @param mockConfigsRequest
     * @return
     */
    @RequestMapping("/syncConfigs")
    public List<MockConfig> getMockConfigs(@RequestBody QueryMockConfigsRequest mockConfigsRequest) {

        MockConfig queryCondition=new MockConfig();
        queryCondition.setEnabled("1");
        List<MockConfig> enabledMockConfigs = mockConfigService.selectMockConfigList(queryCondition);

        List<MockConfig> clientConfigs = mockConfigsRequest.getMockConfigList();
        for (MockConfig clientConfig : clientConfigs) {
            MockConfig mockConfig = mockConfigService.selectMockConfig(clientConfig);
            if(mockConfig==null){
                mockConfigService.insertMockConfig(clientConfig);
            }else {
                clientConfig.setId(mockConfig.getId());
                if(mockConfig.configEnabled()){
                    enabledMockConfigs.add(clientConfig);
                }
            }
        }
        return enabledMockConfigs;
    }

    /**
     * 导出mock配置列表
     */
    @PreAuthorize("@ss.hasPermi('mock:config:export')")
    @Log(title = "mock配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MockConfig mockConfig) {
        List<MockConfig> list = mockConfigService.selectMockConfigList(mockConfig);
        ExcelUtil<MockConfig> util = new ExcelUtil<MockConfig>(MockConfig.class);
        util.exportExcel(response, list, "mock配置数据");
    }

    /**
     * 获取mock配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('mock:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(mockConfigService.selectMockConfigById(id));
    }

    /**
     * 新增mock配置
     */
    @PreAuthorize("@ss.hasPermi('mock:config:add')")
    @Log(title = "mock配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MockConfig mockConfig) {
        return toAjax(mockConfigService.insertMockConfig(mockConfig));
    }

    /**
     * 修改mock配置
     */
    @PreAuthorize("@ss.hasPermi('mock:config:edit')")
    @Log(title = "mock配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MockConfig mockConfig) {
        return toAjax(mockConfigService.updateMockConfig(mockConfig));
    }

    /**
     * 删除mock配置
     */
    @PreAuthorize("@ss.hasPermi('mock:config:remove')")
    @Log(title = "mock配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(mockConfigService.deleteMockConfigByIds(ids));
    }
}
