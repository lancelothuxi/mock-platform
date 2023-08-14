package com.lancelot.mock.api.controller.mock;

import com.alibaba.fastjson.JSONArray;
import com.lancelot.mock.api.controller.domain.MockConfig;
import com.lancelot.mock.api.controller.domain.MockData;
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

    @Test
    public void testSleep() throws Exception{

        ExecutorService executorService = Executors.newCachedThreadPool();

        HashedWheelTimer hashedWheelTimer=new HashedWheelTimer(Executors.defaultThreadFactory(),100,TimeUnit.NANOSECONDS);

        Thread.sleep(1000);


//        delay(200);

        CountDownLatch all=new CountDownLatch(1000);

        for (int i = 0; i < 10000; i++) {

            executorService.submit(new Runnable() {
                @Override
                public void run() {

                    long start = System.currentTimeMillis();
//                    CountDownLatch countDownLatch=new CountDownLatch(1);
//
////                    LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
//
//                    hashedWheelTimer.newTimeout(new TimerTask() {
//                        @Override
//                        public void run(Timeout timeout) throws Exception {
//
//                            countDownLatch.countDown();
//
//
//                        }
//                    }, 300, TimeUnit.MILLISECONDS);
//
//
//                    try {
//                        countDownLatch.await();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    long end = System.currentTimeMillis();

                    System.out.println(" cost =" + (end - start));

                    all.countDown();

//                    System.out.println(" cost ="+(end-start) );
                }
            });
        }


        all.await();


    }


    @Test
    public void testcallLocalHost() throws Exception{

        ExecutorService executorService = Executors.newCachedThreadPool();

        CountDownLatch all=new CountDownLatch(10000);

        for (int i = 0; i < 10000; i++) {

            executorService.submit(new Runnable() {
                @Override
                public void run() {

                    long start = System.currentTimeMillis();

                    send();

                    long end = System.currentTimeMillis();

                    System.out.println(" cost =" + (end - start));

                    all.countDown();

                }
            });
        }

        all.await();

    }



    public void send(){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = "{\"appName\":\"demoConsumer\",\"args\":\"[\\\"2021050205745467\\\",{\\\"delayLevel\\\":0,\\\"errorContent\\\":\\\"{\\\\\\\"state\\\\\\\":30}\\\",\\\"errorType\\\":16,\\\"mainCustId\\\":\\\"2021050205745467\\\",\\\"transactionId\\\":\\\"P230630101345000053\\\"}]\",\"groupName\":\"\",\"interfaceName\":\"com.imooc.springboot.dubbo.demo.DemoService\",\"methodName\":\"sayHello\",\"version\":\"\"}";

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://localhost:8090/mock/dubbo/config/queryMockConfigData")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            okhttp3.Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println("Response: " + responseBody);
            } else {
                System.out.println("Request failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}