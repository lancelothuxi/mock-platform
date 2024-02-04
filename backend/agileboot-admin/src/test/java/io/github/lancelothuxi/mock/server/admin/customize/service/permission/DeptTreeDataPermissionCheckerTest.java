package io.github.lancelothuxi.mock.server.admin.customize.service.permission;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.checker.DeptTreeDataPermissionChecker;
import io.github.lancelothuxi.mock.server.infrastructure.user.web.SystemLoginUser;
import io.github.lancelothuxi.mock.server.infrastructure.user.web.RoleInfo;
import io.github.lancelothuxi.mock.server.admin.customize.service.permission.model.DataCondition;
import io.github.lancelothuxi.mock.server.domain.system.dept.db.SysDeptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeptTreeDataPermissionCheckerTest {

    private final SysDeptService deptService = mock(SysDeptService.class);

    public SystemLoginUser loginUser = mock(SystemLoginUser.class);

    @BeforeEach
    public void mockBefore() {
        when(loginUser.getRoleInfo()).thenReturn(RoleInfo.EMPTY_ROLE);
    }

    @Test
    void testCheckWhenParameterNull() {
        DeptTreeDataPermissionChecker checker = new DeptTreeDataPermissionChecker(deptService);

        boolean check1 = checker.check(null, null);
        boolean check2 = checker.check(new SystemLoginUser(), null);
        boolean check3 = checker.check(null, new DataCondition());
        boolean check4 = checker.check(loginUser, new DataCondition());

        assertFalse(check1);
        assertFalse(check2);
        assertFalse(check3);
        assertFalse(check4);
    }


    @Test
    void testCheckWhenIsChildOfDept() {
        DeptTreeDataPermissionChecker checker = new DeptTreeDataPermissionChecker(deptService);

        when(deptService.isChildOfTheDept(any(), any())).thenReturn(true);
        when(loginUser.getDeptId()).thenReturn(1L);

        DataCondition dataCondition = new DataCondition();
        dataCondition.setTargetDeptId(2L);

        boolean check = checker.check(loginUser, dataCondition);

        assertTrue(check);
    }


    @Test
    void testCheckWhenIsSameDept() {
        DeptTreeDataPermissionChecker checker = new DeptTreeDataPermissionChecker(deptService);

        when(deptService.isChildOfTheDept(any(), any())).thenReturn(false);
        when(loginUser.getDeptId()).thenReturn(1L);
        DataCondition dataCondition = new DataCondition();
        dataCondition.setTargetDeptId(1L);

        boolean check = checker.check(loginUser, dataCondition);

        assertTrue(check);
    }


    @Test
    void testCheckWhenFailed() {
        DeptTreeDataPermissionChecker checker = new DeptTreeDataPermissionChecker(deptService);

        when(deptService.isChildOfTheDept(any(), any())).thenReturn(false);
        when(loginUser.getDeptId()).thenReturn(1L);
        DataCondition dataCondition = new DataCondition();
        dataCondition.setTargetDeptId(2L);

        boolean check = checker.check(loginUser, dataCondition);

        assertFalse(check);
    }




}
