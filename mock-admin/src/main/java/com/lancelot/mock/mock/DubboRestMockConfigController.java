package com.lancelot.mock.mock;

import com.lancelot.mock.api.MockResponse;
import com.lancelot.mock.cache.LocalCache;
import com.lancelot.mock.domain.MockConfig;
import com.lancelot.mock.functions.CompoundVariable;
import com.lancelot.mock.service.IMockConfigService;
import com.lancelot.mock.service.IMockDataService;
import com.lancelot.mock.common.annotation.Log;
import com.lancelot.mock.common.core.controller.BaseController;
import com.lancelot.mock.common.core.domain.AjaxResult;
import com.lancelot.mock.common.core.page.TableDataInfo;
import com.lancelot.mock.common.enums.BusinessType;
import com.lancelot.mock.common.utils.StringUtils;
import com.lancelot.mock.common.utils.poi.ExcelUtil;
import com.lancelot.mock.domain.MockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务mock方法Controller
 *
 * @author ruoyi
 * @date 2023-05-09
 */
@Controller
@RequestMapping("/mock/dubborest/config")
public class DubboRestMockConfigController extends BaseController
{
    private String prefix = "mock/dubborest/config";

    @Autowired
    private IMockConfigService mockConfigService;
    @Autowired
    private IMockDataService mockDataService;

    @Autowired
    private LocalCache localCache;

    @GetMapping()
    public String rule()
    {
        return prefix + "/config";
    }

    /**
     * 查询服务mock方法列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MockConfig MockConfig)
    {
        startPage();
        MockConfig.setType("dubborest");
        List<com.lancelot.mock.domain.MockConfig> list = mockConfigService.fuzzlySelectMockConfigList(MockConfig);
        return getDataTable(list);
    }

    /**
     * 导出服务mock方法列表
     */
    @Log(title = "服务mock方法", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MockConfig MockConfig)
    {
        MockConfig.setType("dubborest");
        List<MockConfig> list = mockConfigService.selectMockConfigList(MockConfig);
        ExcelUtil<MockConfig> util = new ExcelUtil<MockConfig>(MockConfig.class);
        return util.exportExcel(list, "服务mock方法数据");
    }

    /**
     * 新增服务mock方法
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存服务mock方法
     */
    @Log(title = "服务mock方法", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MockConfig MockConfig)
    {
        MockConfig.setType("dubborest");
        return success(mockConfigService.insertMockConfig(MockConfig));
    }

    /**
     * 修改服务mock方法
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        MockConfig MockConfig = mockConfigService.selectMockConfigById(id);
        mmap.put("mockConfig", MockConfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存服务mock方法
     */
    @Log(title = "服务mock方法", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MockConfig MockConfig)
    {
        MockConfig.setType("dubborest");
        return toAjax(mockConfigService.updateMockConfig(MockConfig));
    }

    /**
     * 删除服务mock方法
     */
    @Log(title = "服务mock方法", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(mockConfigService.deleteMockConfigByIds(ids));
    }


    @PostMapping("/batchConfig")
    @ResponseBody
    public List<MockConfig> batchConfig(@RequestBody List<MockConfig> mockConfigList){
        ArrayList<MockConfig> arrayList = new ArrayList<MockConfig>();
        mockConfigList.stream().forEach(mockConfig -> {
            mockConfig.setType("dubborest");
            MockConfig dbConfig = mockConfigService.selectMockConfig(mockConfig);
            if(null != dbConfig){
                arrayList.add(dbConfig);
                return;
            }
            Long id = mockConfigService.insertMockConfig(mockConfig);
            mockConfig.setId(id);
            mockConfig.setEnabled("0");
            arrayList.add(mockConfig);
        });
        return arrayList;
    }


    /**
     * 查询服务mock方法列表
     */
    @PostMapping("/queryMockConfigData")
    @ResponseBody
    public MockResponse queryMockConfigData(@RequestBody MockConfig request) throws Exception{

        long startTime = System.currentTimeMillis();

        request.setEnabled("1");
        request.setType("dubborest");
        MockConfig config = localCache.getMockConfigWithCache(request);
        if(config == null){
            throw new RuntimeException("config not found");
        }

        if(!("1".equals(config.getEnabled()))){
            throw new RuntimeException("config not found");
        }

        final MockData mockData = MockUtil.getMockData(request.getData(), config,config.getMockDataList());
        if(mockData==null){
            MockResponse mockResponse=new MockResponse();
            mockResponse.setCode(-1);
            return mockResponse;
        }

        String data = mockData.getData();
        if(StringUtils.isEmpty(data)){
            MockResponse mockResponse=new MockResponse();
            mockResponse.setCode(-1);
            return mockResponse;
        }


        CompoundVariable compoundVariable= new CompoundVariable();
        compoundVariable.setParameters(data);
        data=compoundVariable.execute(request.getData());
        MockResponse mockResponse=new MockResponse();
        mockResponse.setData(data);


        Integer timeout = mockData.getTimeout();
        if(timeout!=null  && timeout>0){
            MockUtil.sleep(timeout,System.currentTimeMillis()-startTime);
        }

        return mockResponse;
    }





    /**
     * 查询服务mock方法列表
     */
    @PostMapping("/changEnable")
    @ResponseBody
    public AjaxResult changEnable(MockConfig mockConfig)
    {
        return success(mockConfigService.updateMockConfig(mockConfig));
    }
}
