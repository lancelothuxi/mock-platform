package io.github.lancelothuxi.mock.server.integrationTest.db;

import io.github.lancelothuxi.mock.server.domain.system.config.db.SysConfigService;
import io.github.lancelothuxi.mock.server.integrationTest.IntegrationTestApplication;
import io.github.lancelothuxi.mock.server.common.enums.common.ConfigKeyEnum;
import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = IntegrationTestApplication.class)
@RunWith(SpringRunner.class)
class  SysConfigServiceImplTest {

    @Resource
    SysConfigService configService;

    @Test
    void testGetConfigValueByKey() {
        String configValue = configService.getConfigValueByKey(ConfigKeyEnum.CAPTCHA.getValue());
        Assertions.assertFalse(Boolean.parseBoolean(configValue));
    }


}
