package io.github.lancelothuxi.mock.server.domain.system.post.model;

import io.github.lancelothuxi.mock.server.common.exception.ApiException;
import io.github.lancelothuxi.mock.server.domain.system.post.db.SysPostEntity;
import io.github.lancelothuxi.mock.server.domain.system.post.db.SysPostService;
import io.github.lancelothuxi.mock.server.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author valarchie
 */
@Component
@RequiredArgsConstructor
public class PostModelFactory {

    private final SysPostService postService;

    public PostModel loadById(Long postId) {
        SysPostEntity byId = postService.getById(postId);
        if (byId == null) {
            throw new ApiException(ErrorCode.Business.COMMON_OBJECT_NOT_FOUND, postId, "职位");
        }
        return new PostModel(byId, postService);
    }

    public PostModel create() {
        return new PostModel(postService);
    }

}
