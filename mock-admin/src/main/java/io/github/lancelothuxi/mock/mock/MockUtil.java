package io.github.lancelothuxi.mock.mock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPathException;
import io.github.lancelothuxi.mock.domain.MockConfig;
import io.github.lancelothuxi.mock.domain.MockData;
import io.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/6/17 下午12:32
 */
public class MockUtil {


    private static ConcurrentMap<String, MockJSONPath> pathCache = new ConcurrentHashMap<String, MockJSONPath>(128, 0.75f, 1);
    private static ConcurrentMap<String, Object> jsonPathValueMap = new ConcurrentHashMap<String, Object>(128, 0.75f, 1);

    private static Logger logger = LoggerFactory.getLogger(MockUtil.class);

    private static TimeUtil timeUtil = new TimeUtil();

    private static HashedWheelTimer hashedWheelTimer=new HashedWheelTimer(Executors.defaultThreadFactory(),100,TimeUnit.NANOSECONDS);

    public static void sleep(long milliSeconds, long elapsed) throws InterruptedException {
        //如果mock逻辑额外耗时已经超过 milliSeconds 则不需要mock
        if (elapsed >= milliSeconds) {
            return;
        }

        long startTime = System.currentTimeMillis();

        final long expectedCost = milliSeconds - elapsed;

//        CountDownLatch countDownLatch=new CountDownLatch(1);

//        hashedWheelTimer.newTimeout(new TimerTask() {
//            @Override
//            public void run(Timeout timeout) throws Exception {
//                countDownLatch.countDown();
//            }
//        }, expectedCost, TimeUnit.MILLISECONDS);
//
//        countDownLatch.await();

        timeUtil.delay((int) expectedCost);

        long endTime = System.currentTimeMillis();

        final long realCost = endTime - startTime;

        if ((realCost > 10 && realCost > (expectedCost) * 1.2)) {
            logger.error("mock sleep expected={} real={}", expectedCost, realCost);
        }
    }

    public static MockData getMockData(String args, MockConfig mockConfig, List<MockData> mockDataList) {

        Object jsonObject = JSON.parse(args);

        if (mockDataList == null || mockDataList.size() == 0) {
            if (logger.isInfoEnabled()) {
                logger.info("mock mockDataList is empty ");
            }
            return null;
        }

        //精准匹配
        if (mockConfig.getDirectMatch() != null && mockConfig.getDirectMatch() == 1) {

            MockExpression mockExpression = mockDataList.get(0).getMockExpressions().get(0);
            String jsonPath = mockExpression.getJsonPath();

            final Object jsonPathValue = compile(jsonPath).eval(jsonObject);
            if (jsonPathValue == null) {
                return null;
            }

            return mockConfig.getDirMockDataMap().get(jsonPathValue.toString());
        }

        Map<String, String> jsonPathValueMap = new HashMap<>();

        for (MockData mockData : mockDataList) {
            //获取agent所切方法调用的 实际传参
            try {
                boolean checkMatch = andMatch(jsonObject, mockData.getMockExpressions(), jsonPathValueMap);
                if (checkMatch) {
                    return mockData;
                } else {
                    continue;
                }

            } catch (Exception ex) {
                continue;
            }
        }

        return null;
    }


    public static boolean andMatch(Object jsonObject, List<MockExpression> mockExpressions, Map<String, String> jsonPathValueMap) {

        //对实际参数 做jsonPath evaluation 看是否符合 条件
        if (mockExpressions == null || mockExpressions.size() == 0) {
            return true;
        }

        for (MockExpression mockExpression : mockExpressions) {

            String jsonPath = mockExpression.getJsonPath();
            if (!jsonPathValueMap.containsKey(jsonPath)) {
                final Object jsonPathValue = compile(jsonPath).eval(jsonObject);
                jsonPathValueMap.put(jsonPath, jsonPathValue == null ? null : jsonPathValue.toString());
            }

            Object jsonPathValue = jsonPathValueMap.get(jsonPath);

            if (jsonPathValue == null) {
                return false;
            }

            //对实际参数 做jsonPath evaluation 看是否符合 条件
            if (!jsonPathValue.equals(mockExpression.getExpectedValue())) {
                return false;
            }
        }
        return true;
    }


    public static MockJSONPath compile(String path) {
        if (path == null) {
            throw new JSONPathException("jsonpath can not be null");
        }

        MockJSONPath jsonpath = pathCache.get(path);
        if (jsonpath == null) {
            jsonpath = new MockJSONPath(path);
            if (pathCache.size() < 40960) {
                pathCache.putIfAbsent(path, jsonpath);
                jsonpath = pathCache.get(path);
            }
        }
        return jsonpath;
    }
}
