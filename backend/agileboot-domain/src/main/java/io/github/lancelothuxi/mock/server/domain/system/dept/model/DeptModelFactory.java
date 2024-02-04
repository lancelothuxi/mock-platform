package io.github.lancelothuxi.mock.server.domain.system.dept.model;

import io.github.lancelothuxi.mock.server.common.exception.ApiException;
import io.github.lancelothuxi.mock.server.common.exception.error.ErrorCode;
import io.github.lancelothuxi.mock.server.domain.system.dept.command.AddDeptCommand;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptEntity;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 部门模型工厂
 * @author valarchie
 */
@Component
@RequiredArgsConstructor
public class DeptModelFactory {

    private final SysDeptService deptService;

    public DeptModel loadById(Long deptId) {
        SysDeptEntity byId = deptService.getById(deptId);
        if (byId == null) {
            throw new ApiException(ErrorCode.Business.COMMON_OBJECT_NOT_FOUND, deptId, "部门");
        }
        return new DeptModel(byId, deptService);
    }

    public DeptModel create() {
        return new DeptModel(deptService);
    }

    public DeptModel loadFromAddCommand(AddDeptCommand addCommand, DeptModel model) {
        model.setParentId(addCommand.getParentId());
        model.setDeptName(addCommand.getDeptName());
        model.setOrderNum(addCommand.getOrderNum());
        model.setLeaderName(addCommand.getLeaderName());
        model.setPhone(addCommand.getPhone());
        model.setEmail(addCommand.getEmail());
        return model;
    }



}
