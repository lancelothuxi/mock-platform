package io.github.lancelothuxi.mock.mock;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.common.annotation.Log;
import io.github.lancelothuxi.mock.common.core.controller.BaseController;
import io.github.lancelothuxi.mock.common.core.domain.AjaxResult;
import io.github.lancelothuxi.mock.common.core.page.TableDataInfo;
import io.github.lancelothuxi.mock.common.enums.BusinessType;
import io.github.lancelothuxi.mock.common.utils.StringUtils;
import io.github.lancelothuxi.mock.common.utils.poi.ExcelUtil;
import io.github.lancelothuxi.mock.cache.LocalCache;
import io.github.lancelothuxi.mock.domain.MockConfig;
import io.github.lancelothuxi.mock.domain.MockData;
import io.github.lancelothuxi.mock.service.IMockConfigService;
import io.github.lancelothuxi.mock.service.IMockDataService;
import io.github.lancelothuxi.mock.functions.CompoundVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static io.github.lancelothuxi.mock.mock.MockUtil.getMockData;

/**
 * 服务mock方法Controller
 *
 * @author lancelot huxisuz@gmail.com
 * @date 2023-05-09
 */
@Controller
@RequestMapping("/mock/feign/config")
public class FeignMockConfigController extends BaseController
{

    private static Logger logger= LoggerFactory.getLogger(MockConfigController.class);


    private String prefix = "mock/feign/config";

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
        MockConfig.setType("feign");
        List<io.github.lancelothuxi.mock.domain.MockConfig> list = mockConfigService.fuzzlySelectMockConfigList(MockConfig);
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
        MockConfig.setType("feign");
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
        MockConfig.setType("feign");
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
    public MockFeignResponse queryMockConfigData(@RequestBody MockConfig request) throws InterruptedException {

        long startTime = System.currentTimeMillis();
        String interfaceName = request.getInterfaceName();
        String methodName = request.getMethodName();

        //监控结束
        logger.info("feign mock mockRequest={}", JSON.toJSONString(request));

        request.setEnabled("1");
        request.setType("feign");
        MockConfig config = localCache.getMockConfigWithCache(request);
        logger.info("feign mock mockResponse={}", JSON.toJSONString(config));
        if(config == null){
            MockFeignResponse mockResponse=new MockFeignResponse();
            mockResponse.setCode(-1);
            return mockResponse;
        }
        if(!("1".equals(config.getEnabled()))){
            MockFeignResponse mockResponse=new MockFeignResponse();
            mockResponse.setCode(-1);
            return mockResponse;
        }

        final MockData mockData = getMockData(request.getData(), config,config.getMockDataList());
        if(mockData==null){
            MockFeignResponse mockResponse=new MockFeignResponse();
            mockResponse.setCode(-1);
            return mockResponse;
        }

        String data = mockData.getData();
        if(StringUtils.isEmpty(data)){
            MockFeignResponse mockResponse=new MockFeignResponse();
            mockResponse.setCode(-1);
            return mockResponse;
        }

        CompoundVariable compoundVariable= new CompoundVariable();
        compoundVariable.setParameters(data);
        data=compoundVariable.execute(request.getData());

        MockFeignResponse mockResponse=new MockFeignResponse();
        mockResponse.setData(data);

        Integer timeout = mockData.getTimeout();


        if(timeout!=null  && timeout>0){
            MockUtil.sleep(timeout,System.currentTimeMillis()-startTime);
        }

        long endTime = System.currentTimeMillis();
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
