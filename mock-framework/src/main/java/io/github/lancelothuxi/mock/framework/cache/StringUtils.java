package io.github.lancelothuxi.mock.framework.cache;

/**
 * @author liulei1971
 * @date 2023/5/26 11:40
 */
public class StringUtils {

    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = newStringBuilder(noOfItems);

                for(int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    if (array[i] != null) {
                        buf.append(array[i]);
                    }
                }

                return buf.toString();
            }
        }
    }

    public static String join(Object[] array, char separator) {
        return array == null ? null : join((Object[])array, separator, 0, array.length);
    }

    private static StringBuilder newStringBuilder(int noOfItems) {
        return new StringBuilder(noOfItems * 16);
    }
    public static <T> String join(T... elements) {
        return join((Object[])elements, (String)null);
    }
}
