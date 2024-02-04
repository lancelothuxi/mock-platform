package io.github.lancelothuxi.mock.server.domain.system.user.dto;

import io.github.lancelothuxi.mock.server.domain.system.role.dto.RoleDTO;
import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class UserInfoDTO {

    private UserDTO user;
    private RoleDTO role;

}
