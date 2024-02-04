package io.github.lancelothuxi.mock.server.domain.system.user.dto;

import io.github.lancelothuxi.mock.server.domain.system.post.db.SysPostEntity;
import io.github.lancelothuxi.mock.server.domain.system.role.db.SysRoleEntity;
import io.github.lancelothuxi.mock.server.domain.system.user.db.SysUserEntity;
import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class UserProfileDTO {

    public UserProfileDTO(SysUserEntity userEntity, SysPostEntity postEntity, SysRoleEntity roleEntity) {
        if (userEntity != null) {
            this.user = new UserDTO(userEntity);
        }

        if (postEntity != null) {
            this.postName = postEntity.getPostName();
        }

        if (roleEntity != null) {
            this.roleName = roleEntity.getRoleName();
        }
    }

    private UserDTO user;
    private String roleName;
    private String postName;

}
