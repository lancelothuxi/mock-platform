package io.github.lancelothuxi.mock.cache;

import com.google.common.base.Joiner;

/**
 * @author lancelot
 * @version 1.0
 * @since 2023/7/18 上午12:33
 */
public class CacheUtil {

  public static final String CONFIG_PREFX = "mockServer:mockConfig:";

  public static String buildKey4Config(String... args) {
    return CONFIG_PREFX + Joiner.on("_").useForNull("null").join(args);
  }
}
