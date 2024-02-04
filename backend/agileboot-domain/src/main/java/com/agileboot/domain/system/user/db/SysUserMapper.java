package com.agileboot.domain.system.user.db;

import com.agileboot.domain.system.post.db.SysPostEntity;
import com.agileboot.domain.system.role.db.SysRoleEntity;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户名
     * @return 角色列表
     */
    @Select("SELECT DISTINCT r.* "
        + "FROM sys_role r "
        + " LEFT JOIN sys_user u ON u.role_id = r.role_id "
        + "WHERE r.deleted = 0 "
        + " AND u.user_id = #{userId}")
    List<SysRoleEntity> getRolesByUserId(Long userId);

    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户名
     * @return 结果
     */
    @Select("SELECT p.* "
        + "FROM sys_post p "
        + " LEFT JOIN sys_user u ON p.post_id = u.post_id "
        + "WHERE u.user_id = #{userId} "
        + " AND p.deleted = 0")
    List<SysPostEntity> getPostsByUserId(Long userId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Select("SELECT DISTINCT m.perms "
        + "FROM sys_menu m "
        + " LEFT JOIN sys_role_menu rm ON m.menu_id = rm.menu_id "
        + " LEFT JOIN sys_user u ON rm.role_id = u.role_id "
        + " LEFT JOIN sys_role r ON r.role_id = u.role_id "
        + "WHERE m.status = 1 AND m.deleted = 0 "
        + " AND r.status = 1 AND r.deleted = 0 "
        + " AND u.user_id = #{userId}")
    Set<String> getMenuPermsByUserId(Long userId);

    /**
     * 根据条件分页查询角色关联的用户列表
     *
     * @param page         分页对象
     * @param queryWrapper 条件选择器
     * @return 分页处理后的用户列表
     */
    @Select("SELECT DISTINCT u.user_id, u.dept_id, u.username, u.nick_name, u.email "
        + " , u.phone_number, u.status, u.create_time "
        + "FROM sys_user u "
        + " LEFT JOIN sys_dept d ON u.dept_id = d.dept_id "
        + " LEFT JOIN sys_role r ON r.role_id = u.role_id"
        + " ${ew.customSqlSegment}")
    Page<SysUserEntity> getUserListByRole(Page<SysUserEntity> page,
        @Param(Constants.WRAPPER) Wrapper<SysUserEntity> queryWrapper);

    /**
     * 根据条件分页查询用户列表
     * @param page 页码对象
     * @param queryWrapper 查询对象
     * @return 用户信息集合信息
     */
    @Select("SELECT u.*, d.dept_name, d.leader_name as dept_leader "
        + "FROM sys_user u "
        + " LEFT JOIN sys_dept d ON u.dept_id = d.dept_id "
        + "${ew.customSqlSegment}")
    Page<SearchUserDO> getUserList(Page<SearchUserDO> page,
        @Param(Constants.WRAPPER) Wrapper<SearchUserDO> queryWrapper);

}
