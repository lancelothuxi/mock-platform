package com.lancelot.mock.mock;

import com.alibaba.fastjson.JSONArray;
import com.lancelot.mock.domain.MockConfig;
import com.lancelot.mock.domain.MockData;
import io.netty.util.HashedWheelTimer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.alibaba.fastjson.JSON;

import java.util.List;
/**
 * @author lancelot
 * @version 1.0
 * @date 2023/7/28 下午5:31
 */
public class MockUtilTest {

    @Test
    public void testAndMatch() {
        // 模拟输入的 jsonObject
        Object jsonObject =JSON.parse("{\"key1\":\"value1\",\"key2\":\"value2\"}");

        // 模拟输入的 mockReqParams
        String mockReqParams = "[{\"jsonPath\":\"$.key1\",\"expectedValue\":\"value1\"}, {\"jsonPath\":\"$.key2\",\"expectedValue\":\"value2\"}]";

        // 模拟解析 mockReqParams 返回的 mockExpressions

        boolean andMatch = MockUtil.andMatch(jsonObject, JSONArray.parseArray(mockReqParams,MockExpression.class),new HashMap<>());

        Assert.assertTrue(andMatch);
    }


    @Test()
    public void testGetMockData(){

        String args="{\"key1\":\"value999\",\"key2\":\"value2\"}";

        MockConfig mockConfig=new MockConfig();
        mockConfig.setMethodName("methodName");

        List<MockData> mockDatas=new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            MockData mockData=new MockData();
            mockData.setData("mydata");
            mockData.setMockReqParams("[{\"expectedValue\":\"value1\",\"jsonPath\":\"$.key1\"}]");
            mockData.setMockExpressions(JSONArray.parseArray(mockData.getMockReqParams(),MockExpression.class));
            mockDatas.add(mockData);
        }


        for (int i = 0; i < 1000; i++) {
            long start = System.currentTimeMillis();
            MockJSONPath.compile("$.key1");

            MockData mockData = MockUtil.getMockData(args, mockConfig, mockDatas);

            long endTime = System.currentTimeMillis();

            System.out.println("mockData = " + mockData+" and cost="+(endTime-start));
        }


    }
}