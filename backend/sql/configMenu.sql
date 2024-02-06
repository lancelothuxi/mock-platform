-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('mock配置', '3', '1', 'config', 'mock/config/index', 1, 0, 'C', '0', '0', 'mock:config:list', '#', 'admin', sysdate(), '', null, 'mock配置菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('mock配置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'mock:config:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('mock配置新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'mock:config:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('mock配置修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'mock:config:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('mock配置删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'mock:config:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('mock配置导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'mock:config:export',       '#', 'admin', sysdate(), '', null, '');