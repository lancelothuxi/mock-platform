package io.github.lancelothuxi.mock.mock;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/7/17 下午10:54
 */
public class MockMyabtisCache implements Cache {

    private static Logger logger= LoggerFactory.getLogger(MockMyabtisCache.class);
    private ConcurrentHashMap<Object,Object> cache = new ConcurrentHashMap<Object, Object>();

    public MockMyabtisCache(String name) {

    }

    @Override
    public String getId() {
        return "MockMyabtisCache00";
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key,value);
    }

    @Override
    public Object getObject(Object key) {
        final Object value = cache.get(key);
        if (value == null) {
            logger.info("cache miss key={} ",key);
        }
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        logger.info("cache clear");
        cache.clear();
    }

    @Override
    public int getSize() {
        return cache.size();
    }
}
