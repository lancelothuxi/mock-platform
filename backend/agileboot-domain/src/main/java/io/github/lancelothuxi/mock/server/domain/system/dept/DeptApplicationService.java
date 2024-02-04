package io.github.lancelothuxi.mock.server.domain.system.dept;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import io.github.lancelothuxi.mock.server.domain.system.dept.command.AddDeptCommand;
import io.github.lancelothuxi.mock.server.domain.system.dept.command.UpdateDeptCommand;
import io.github.lancelothuxi.mock.server.domain.system.dept.dto.DeptDTO;
import io.github.lancelothuxi.mock.server.domain.system.dept.model.DeptModel;
import io.github.lancelothuxi.mock.server.domain.system.dept.model.DeptModelFactory;
import io.github.lancelothuxi.mock.server.domain.system.dept.query.DeptQuery;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptEntity;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptService;
import io.github.lancelothuxi.mock.server.domain.system.role.db.SysRoleService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 部门服务
 * @author valarchie
 */
@Service
@RequiredArgsConstructor
public class DeptApplicationService {

    private final SysDeptService deptService;

    private final SysRoleService roleService;

    private final DeptModelFactory deptModelFactory;


    public List<DeptDTO> getDeptList(DeptQuery query) {
        List<SysDeptEntity> list = deptService.list(query.toQueryWrapper());
        return list.stream().map(DeptDTO::new).collect(Collectors.toList());
    }

    public DeptDTO getDeptInfo(Long id) {
        SysDeptEntity byId = deptService.getById(id);
        return new DeptDTO(byId);
    }

    public List<Tree<Long>> getDeptTree() {
        List<SysDeptEntity> list = deptService.list();

        return TreeUtil.build(list, 0L, (dept, tree) -> {
            tree.setId(dept.getDeptId());
            tree.setParentId(dept.getParentId());
            tree.putExtra("label", dept.getDeptName());
        });
    }


    public void addDept(AddDeptCommand addCommand) {
        DeptModel deptModel = deptModelFactory.create();
        deptModel.loadAddCommand(addCommand);

        deptModel.checkDeptNameUnique();
        deptModel.generateAncestors();

        deptModel.insert();
    }

    public void updateDept(UpdateDeptCommand updateCommand) {
        DeptModel deptModel = deptModelFactory.loadById(updateCommand.getDeptId());
        deptModel.loadUpdateCommand(updateCommand);

        deptModel.checkDeptNameUnique();
        deptModel.checkParentIdConflict();
        deptModel.checkStatusAllowChange();
        deptModel.generateAncestors();

        deptModel.updateById();
    }

    public void removeDept(Long deptId) {
        DeptModel deptModel = deptModelFactory.loadById(deptId);

        deptModel.checkHasChildDept();
        deptModel.checkDeptAssignedToUsers();

        deptModel.deleteById();
    }



}
