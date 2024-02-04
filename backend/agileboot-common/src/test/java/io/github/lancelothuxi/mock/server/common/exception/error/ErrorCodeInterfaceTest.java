package io.github.lancelothuxi.mock.server.common.exception.error;

import io.github.lancelothuxi.mock.server.common.exception.error.ErrorCode.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ErrorCodeInterfaceTest {

    @Test
    void testI18nKey() {
        String i18nKey = Client.COMMON_FORBIDDEN_TO_CALL.i18nKey();
        Assertions.assertEquals("20001_COMMON_FORBIDDEN_TO_CALL", i18nKey);
    }
}
