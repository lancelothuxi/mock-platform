package io.github.lancelothuxi.mock.cache;

import com.google.common.base.Joiner;

import java.util.Arrays;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/7/18 上午12:33
 */
public class CacheUtil {

    public static final String CONFIG_PREFX="mockServer:mockConfig:";
    public static final String DATA_PREFX="mockServer:mockData:";


    public static String buildKey4Config(String... args){
        return CONFIG_PREFX+Joiner
                .on("_").useForNull("null")
                        .join(args);
    }
}
