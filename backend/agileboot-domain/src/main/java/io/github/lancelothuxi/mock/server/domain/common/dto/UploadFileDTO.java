package io.github.lancelothuxi.mock.server.domain.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author valarchie
 */
@Data
@NoArgsConstructor
public class UploadFileDTO {

    public UploadFileDTO(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private String imgUrl;

}
