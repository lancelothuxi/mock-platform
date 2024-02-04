package io.github.lancelothuxi.mock.server.domain.system.user.command;

import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class ResetPasswordCommand {

    private Long userId;
    private String password;

}
