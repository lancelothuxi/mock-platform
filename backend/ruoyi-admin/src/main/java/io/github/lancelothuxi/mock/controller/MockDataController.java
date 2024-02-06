package io.github.lancelothuxi.mock.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import io.github.lancelothuxi.mock.domain.MockData;
import io.github.lancelothuxi.mock.service.IMockDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * mock数据Controller
 *
 * @author ruoyi
 * @date 2024-02-06
 */
@RestController
@RequestMapping("/mock/data")
public class MockDataController extends BaseController {
    @Autowired
    private IMockDataService mockDataService;

    /**
     * 查询mock数据列表
     */
    @PreAuthorize("@ss.hasPermi('mock:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(MockData mockData) {
        startPage();
        List<MockData> list = mockDataService.selectMockDataList(mockData);
        return getDataTable(list);
    }

    /**
     * 导出mock数据列表
     */
    @PreAuthorize("@ss.hasPermi('mock:data:export')")
    @Log(title = "mock数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MockData mockData) {
        List<MockData> list = mockDataService.selectMockDataList(mockData);
        ExcelUtil<MockData> util = new ExcelUtil<MockData>(MockData.class);
        util.exportExcel(response, list, "mock数据数据");
    }

    /**
     * 获取mock数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('mock:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(mockDataService.selectMockDataById(id));
    }

    /**
     * 新增mock数据
     */
    @PreAuthorize("@ss.hasPermi('mock:data:add')")
    @Log(title = "mock数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MockData mockData) {
        return toAjax(mockDataService.insertMockData(mockData));
    }

    /**
     * 修改mock数据
     */
    @PreAuthorize("@ss.hasPermi('mock:data:edit')")
    @Log(title = "mock数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MockData mockData) {
        return toAjax(mockDataService.updateMockData(mockData));
    }

    /**
     * 删除mock数据
     */
    @PreAuthorize("@ss.hasPermi('mock:data:remove')")
    @Log(title = "mock数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(mockDataService.deleteMockDataByIds(ids));
    }
}
