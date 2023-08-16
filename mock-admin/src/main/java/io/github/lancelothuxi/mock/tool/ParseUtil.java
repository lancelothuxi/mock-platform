package io.github.lancelothuxi.mock.tool;

import com.fasterxml.jackson.databind.JavaType;
import org.apache.commons.lang3.StringUtils;

public class ParseUtil {
    public static Object parseMockValue(String mock, JavaType type) throws Exception {
        Object value = null;
       if ("null".equals(mock)) {
            value = null;
        } else if ("true".equals(mock)) {
            value = true;
        } else if ("false".equals(mock)) {
            value = false;
        } else if (mock.length() < 2 || (!mock.startsWith("\"") || !mock.endsWith("\"")) && (!mock.startsWith("'") || !mock.endsWith("'"))) {
            if (type != null && type.getRawClass() == String.class) {
                value = mock;
            } else if (StringUtils.isNumeric(mock)) {
                value = JsonUtils.parseObject(mock);
            } else if (mock.startsWith("{")) {
                value = JsonUtils.parseObject(mock, type);
            } else if (mock.startsWith("[")) {
                value = JsonUtils.parseObject(mock, type);
            } else {
                value = mock;
            }
        } else {
            value = mock.subSequence(1, mock.length() - 1);
        }

        return value;
    }
}
