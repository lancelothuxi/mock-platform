package io.github.lancelothuxi.mock.mock;

import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.cache.LocalCache;
import io.github.lancelothuxi.mock.domain.MockConfig;
import io.github.lancelothuxi.mock.common.utils.StringUtils;
import io.github.lancelothuxi.mock.functions.CompoundVariable;
import io.github.lancelothuxi.mock.service.IMockConfigService;
import io.github.lancelothuxi.mock.common.core.page.TableDataInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.lancelothuxi.mock.common.annotation.Log;
import io.github.lancelothuxi.mock.common.enums.BusinessType;
import io.github.lancelothuxi.mock.domain.MockData;
import io.github.lancelothuxi.mock.service.IMockDataService;
import io.github.lancelothuxi.mock.common.core.controller.BaseController;
import io.github.lancelothuxi.mock.common.core.domain.AjaxResult;
import io.github.lancelothuxi.mock.common.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * mock配置关联响应数据Controller
 * @author lancelot huxisuz@gmail.com
 * @date 2023-05-10
 */
@Controller
@RequestMapping("/mock/data")
public class MockDataController extends BaseController
{
    private String prefix = "mock/data";

    @Autowired
    private IMockDataService mockDataService;

    @Autowired
    private IMockConfigService mockConfigService;

    @Autowired
    private LocalCache localCache;

    @GetMapping()
    public String data(MockData mockData,ModelMap modelMap)
    {
        modelMap.put("mockConfigId",mockData.getMockConfigId());
        return prefix + "/data";
    }

    /**
     * 查询mock配置关联响应数据列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MockData mockData)
    {

        MockConfig mockConfig = mockConfigService.selectMockConfigById(Long.valueOf(mockData.getMockConfigId()));
        startPage();
        List<MockData> list = mockDataService.selectMockDataList(mockData);
        if(list!=null){
            for (MockData data : list) {
                data.setApplicationName(mockConfig.getAppliactionName());
                data.setMockConfigId(mockData.getMockConfigId());
            }
        }
        return getDataTable(list);
    }


    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<MockData> util = new ExcelUtil<MockData>(MockData.class);
        return util.importTemplateExcel("mock数据");
    }

    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file,MockData mockData) throws Exception {

        MockConfig mockConfig = mockConfigService.selectMockConfigById(Long.valueOf(mockData.getMockConfigId()));
        if(mockConfig==null){
            return AjaxResult.error("mock config 为空");
        }

        if(logger.isInfoEnabled()){
            logger.info("开始读取excel数据 mockConfig={}", JSON.toJSONString(mockConfig));
        }

        List<MockData> mockDatas = EasyExcel.read(file.getInputStream()).head(MockData.class).sheet().doReadSync();


        if(logger.isInfoEnabled()){
            logger.info("开始导入数据 mockConfig={},总size={}", JSON.toJSONString(mockConfig),mockDatas.size());
        }
        try {
            mockDataService.importMockData(mockDatas,mockData.getMockConfigId());
        }catch (Exception ex){
            return AjaxResult.error(ex.getMessage());
        }
        //更新同时通知缓存更新
        mockConfigService.updateMockConfig(mockConfig);

        return AjaxResult.success();
    }

    /**
     * 导出mock配置关联响应数据列表
     */
    @Log(title = "mock配置关联响应数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MockData mockData)
    {
        List<MockData> list = mockDataService.selectMockDataList(mockData);
        ExcelUtil<MockData> util = new ExcelUtil<MockData>(MockData.class);
        return util.exportExcel(list, "mock配置关联响应数据数据");
    }

    /**
     * 新增mock配置关联响应数据
     */
    @GetMapping("/add")
    public String add(MockData mockData,ModelMap modelMap)
    {
        modelMap.put("mockConfigId",mockData.getMockConfigId());
        return prefix + "/add";
    }


    /**
     * 新增mock配置关联响应数据
     */
    @GetMapping("/batchUpdateMockTime")
    public String batchUpdateMockTime(MockData mockData,ModelMap modelMap) {

        MockConfig mockConfigRequest = mockConfigService.selectMockConfigById(Long.valueOf(mockData.getMockConfigId()));
        MockConfig mockConfigWithCache = localCache.getMockConfigWithCache(mockConfigRequest);
        List<MockData> mockDataList = mockConfigWithCache.getMockDataList();
        if(CollectionUtils.isEmpty(mockDataList)){
            throw new RuntimeException("该mock规则没有配置mock数据");
        }

        modelMap.put("mockConfigId",mockData.getMockConfigId());
        modelMap.put("timeout",mockDataList.get(0).getTimeout());

        return prefix + "/modifyMockTime";
    }


    /**
     * 新增mock配置关联响应数据
     */
    @PostMapping("/doModifyMockTime")
    @ResponseBody
    public AjaxResult doModifyMockTime(MockData mockData,ModelMap modelMap) {

        mockDataService.batchUpdateMockDelayTimeByMockId(mockData.getMockConfigId(),mockData.getTimeout());

        //触发缓存刷新
        MockConfig mockConfig = mockConfigService.selectMockConfigById(Long.valueOf(mockData.getMockConfigId()));
        mockConfigService.updateMockConfig(mockConfig);
        return AjaxResult.success();
    }





    /**
     * 新增保存mock配置关联响应数据
     */
    @Log(title = "mock配置关联响应数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MockData mockData)
    {
        if(StringUtils.isEmpty(mockData.getData())){
            return AjaxResult.error("数据不能为空");
        }

        try {
            CompoundVariable compoundVariable= new CompoundVariable();
            compoundVariable.setParameters(mockData.getData());
            compoundVariable.execute();
        }catch (Exception ex){
            return AjaxResult.error("非法的函数："+ex.getMessage());
        }

        return toAjax(mockDataService.insertMockData(mockData));
    }

    /**
     * 修改mock配置关联响应数据
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        MockData mockData = mockDataService.selectMockDataById(id);
        mmap.put("mockData", mockData);
        return prefix + "/edit";
    }

    /**
     * 修改保存mock配置关联响应数据
     */
    @Log(title = "mock配置关联响应数据", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MockData mockData)
    {

        if(StringUtils.isEmpty(mockData.getData())){
            return AjaxResult.error("数据不能为空");
        }

        try {
            CompoundVariable compoundVariable= new CompoundVariable();
            compoundVariable.setParameters(mockData.getData());
            compoundVariable.execute();
        }catch (Exception ex){
            return AjaxResult.error("非法的函数："+ex.getMessage());
        }


        return toAjax(mockDataService.updateMockData(mockData));
    }

    /**
     * 删除mock配置关联响应数据
     */
    @Log(title = "mock配置关联响应数据", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(mockDataService.deleteMockDataByIds(ids));
    }

    @PostMapping("/changEnable")
    @ResponseBody
    public AjaxResult changEnable(MockData mockData)
    {
        return success(mockDataService.updateMockData(mockData));
    }

}
