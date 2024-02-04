package io.github.lancelothuxi.mock.server.admin.customize.service.login.dto;

import io.github.lancelothuxi.mock.server.common.enums.dictionary.DictionaryData;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class ConfigDTO {

    private Boolean isCaptchaOn;

    private Map<String, List<DictionaryData>> dictionary;

}
