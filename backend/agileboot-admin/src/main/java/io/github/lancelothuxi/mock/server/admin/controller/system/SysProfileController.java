package io.github.lancelothuxi.mock.server.admin.controller.system;

import io.github.lancelothuxi.mock.server.common.constant.Constants.UploadSubDir;
import io.github.lancelothuxi.mock.server.common.core.base.BaseController;
import io.github.lancelothuxi.mock.server.common.core.dto.ResponseDTO;
import io.github.lancelothuxi.mock.server.common.exception.ApiException;
import io.github.lancelothuxi.mock.server.common.exception.error.ErrorCode;
import io.github.lancelothuxi.mock.server.common.utils.file.FileUploadUtils;
import io.github.lancelothuxi.mock.server.domain.common.dto.UploadFileDTO;
import io.github.lancelothuxi.mock.server.domain.system.user.UserApplicationService;
import io.github.lancelothuxi.mock.server.domain.system.user.command.UpdateProfileCommand;
import io.github.lancelothuxi.mock.server.domain.system.user.command.UpdateUserAvatarCommand;
import io.github.lancelothuxi.mock.server.domain.system.user.command.UpdateUserPasswordCommand;
import io.github.lancelothuxi.mock.server.domain.system.user.dto.UserProfileDTO;
import io.github.lancelothuxi.mock.server.admin.customize.aop.accessLog.AccessLog;
import io.github.lancelothuxi.mock.server.infrastructure.user.AuthenticationUtils;
import io.github.lancelothuxi.mock.server.infrastructure.user.web.SystemLoginUser;
import io.github.lancelothuxi.mock.server.common.enums.common.BusinessTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@Tag(name = "个人信息API", description = "个人信息相关接口")
@RestController
@RequestMapping("/system/user/profile")
@RequiredArgsConstructor
public class SysProfileController extends BaseController {

    private final UserApplicationService userApplicationService;

    /**
     * 个人信息
     */
    @Operation(summary = "获取个人信息")
    @GetMapping
    public ResponseDTO<UserProfileDTO> profile() {
        SystemLoginUser user = AuthenticationUtils.getSystemLoginUser();
        UserProfileDTO userProfile = userApplicationService.getUserProfile(user.getUserId());
        return ResponseDTO.ok(userProfile);
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改个人信息")
    @AccessLog(title = "个人信息", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping
    public ResponseDTO<Void> updateProfile(@RequestBody UpdateProfileCommand command) {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        command.setUserId(loginUser.getUserId());
        userApplicationService.updateUserProfile(command);
        return ResponseDTO.ok();
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置个人密码")
    @AccessLog(title = "个人信息", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("/password")
    public ResponseDTO<Void> updatePassword(@RequestBody UpdateUserPasswordCommand command) {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        command.setUserId(loginUser.getUserId());
        userApplicationService.updatePasswordBySelf(loginUser, command);
        return ResponseDTO.ok();
    }

    /**
     * 头像上传
     */
    @Operation(summary = "修改个人头像")
    @AccessLog(title = "用户头像", businessType = BusinessTypeEnum.MODIFY)
    @PostMapping("/avatar")
    public ResponseDTO<UploadFileDTO> avatar(@RequestParam("avatarfile") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApiException(ErrorCode.Business.USER_UPLOAD_FILE_FAILED);
        }
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        String avatarUrl = FileUploadUtils.upload(UploadSubDir.AVATAR_PATH, file);

        userApplicationService.updateUserAvatar(new UpdateUserAvatarCommand(loginUser.getUserId(), avatarUrl));
        return ResponseDTO.ok(new UploadFileDTO(avatarUrl));
    }
}
