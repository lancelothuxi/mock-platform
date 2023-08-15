package com.lancelot.mock.mock;

import com.alibaba.fastjson.JSON;
import com.lancelot.mock.api.CommonDubboMockService;
import com.lancelot.mock.api.MockRequest;
import com.lancelot.mock.api.MockResponse;
import com.lancelot.mock.common.annotation.Log;
import com.lancelot.mock.common.core.controller.BaseController;
import com.lancelot.mock.common.core.domain.AjaxResult;
import com.lancelot.mock.common.core.page.TableDataInfo;
import com.lancelot.mock.common.core.text.Convert;
import com.lancelot.mock.common.enums.BusinessType;
import com.lancelot.mock.common.utils.poi.ExcelUtil;
import com.lancelot.mock.domain.MockConfig;
import com.lancelot.mock.domain.MockData;
import com.lancelot.mock.service.IMockConfigService;
import com.lancelot.mock.service.IMockDataService;
import com.lancelot.mock.vo.MockConfigVo;
import com.lancelot.mock.vo.MockDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

/**
 * 服务mock方法Controller
 *
 * @author ruoyi
 * @date 2023-05-09
 */
@Controller
@RequestMapping("/mock/dubbo/config")
public class MockConfigController extends BaseController
{
    private String prefix = "mock/dubbo/config";

    @Autowired
    private IMockConfigService mockConfigService;

    @Autowired
    private IMockDataService mockDataService;

    @Autowired
    private CommonDubboMockService commonDubboMockService;


    @GetMapping()
    public String rule(MockConfig mockConfig,ModelMap modelMap)
    {
        modelMap.put("type", defaultIfNull(mockConfig.getType(),"dubbo"));
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


    @PostMapping( "/batchOpen")
    @ResponseBody
    public AjaxResult batchOpen(String ids) {
        String[] idArray = Convert.toStrArray(ids);
        for (String s : idArray) {
            MockConfig real = mockConfigService.selectMockConfigById(Long.valueOf(s));
            if(real!=null){
                real.setEnabled("1");
            }

            mockConfigService.updateMockConfig(real);
        }
        return AjaxResult.success();
    }


    @PostMapping( "/batchClose")
    @ResponseBody
    public AjaxResult batchClose(String ids)
    {
        String[] idArray = Convert.toStrArray(ids);
        for (String s : idArray) {
            MockConfig real = mockConfigService.selectMockConfigById(Long.valueOf(s));
            if(real!=null){
                real.setEnabled("0");
            }

            mockConfigService.updateMockConfig(real);
        }
        return AjaxResult.success();
    }


    @PostMapping("/register")
    @ResponseBody
    public List<MockConfig> register(@RequestBody Request request){

        logger.info("register mock config {}", JSON.toJSONString(request));

        ArrayList<MockConfig> arrayList = new ArrayList<MockConfig>();

        try {

            request.getMockConfigList().stream().forEach(mockConfig -> {
                mockConfig.setAppliactionName(request.getAppName());
                mockConfig.setType(request.getType());
                MockConfig dbConfig = mockConfigService.selectMockConfig(mockConfig);
                if(null != dbConfig){
                    return;
                }
                Long id = mockConfigService.insertMockConfig(mockConfig);
                mockConfig.setType(request.getType());
                mockConfig.setId(id);
                mockConfig.setEnabled("0");
                arrayList.add(mockConfig);
            });
        }catch (Exception ex){
            logger.info("register mock config error {}", JSON.toJSONString(request),ex);
        }

        return arrayList;
    }

    @PostMapping("/getAllByType")
    @ResponseBody
    public Response getAllByType(@RequestBody Request request){

        MockConfig mockConfig=new MockConfig();
        mockConfig.setType(request.getType());
        mockConfig.setEnabled("1");
        mockConfig.setAppliactionName(request.getAppName());

        final List<MockConfig> mockConfigs = mockConfigService.selectMockConfigList(mockConfig);
        if(CollectionUtils.isEmpty(mockConfigs)){
            return new Response();
        }

        final List<String> ids = mockConfigs.stream().map(t->t.getId()+"").collect(Collectors.toList());
        final List<MockData> mockDataList = mockDataService.selectListByConfigList(ids);


        final List<MockConfigVo> mockConfigVos =new ArrayList<>();

        for (MockConfig config : mockConfigs) {

            MockConfigVo mockConfigVo = new MockConfigVo();
            mockConfigVo.setId(config.getId());
            mockConfigVo.setInterfaceName(config.getInterfaceName());
            mockConfigVo.setMethodName(config.getMethodName());
            mockConfigVo.setGroupName(config.getGroupName());
            mockConfigVo.setVersion(config.getVersion());
            mockConfigVo.setServerSideMock(config.getServerSideMock());
            mockConfigVo.setAppliactionName(config.getAppliactionName());
            mockConfigVos.add(mockConfigVo);

            for (MockData mockData : mockDataList) {
                if(config.getId().toString().equals(mockData.getMockConfigId())){
                    MockDataVo mockDataVo=new MockDataVo();
                    mockDataVo.setData(mockData.getData());
                    mockDataVo.setMockReqParams(mockData.getMockReqParams());
                    mockDataVo.setTimeout(mockData.getTimeout());
                    mockDataVo.setExpectedValue(mockData.getExpectedValue());
                    mockConfigVo.getMockDataList().add(mockDataVo);
                }
            }
        }

        Response response=new Response();
        response.setMockConfigs(mockConfigVos);
        return response;
    }


    /**
     * 查询服务mock方法列表
     */
    @PostMapping("/queryMockConfigData")
    @ResponseBody
    public MockResponse queryMockConfigData(@RequestBody MockRequest request)
    {
        return commonDubboMockService.doMockRequest(request);
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


    /**
     * 查询服务mock方法列表
     */
    @PostMapping("/directMatchSwitch")
    @ResponseBody
    public AjaxResult directMatchSwitch(MockConfig mockConfig)
    {
        return success(mockConfigService.updateMockConfig(mockConfig));
    }


}
