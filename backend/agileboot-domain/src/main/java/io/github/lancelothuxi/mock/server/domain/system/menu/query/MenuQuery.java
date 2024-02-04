package io.github.lancelothuxi.mock.server.domain.system.menu.query;

import io.github.lancelothuxi.mock.server.common.core.page.AbstractQuery;
import io.github.lancelothuxi.mock.server.domain.system.menu.db.SysMenuEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuQuery extends AbstractQuery<SysMenuEntity> {
      // 直接交给前端筛选
//    private String menuName;
//    private Boolean isVisible;
//    private Integer status;
    private Boolean isButton;

    @Override
    public QueryWrapper<SysMenuEntity> addQueryCondition() {
        QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(isButton != null, "is_button", isButton);
//            .like(StrUtil.isNotEmpty(menuName), "menu_name", menuName)
//            .eq(isVisible != null, "is_visible", isVisible)
//            .eq(status != null, "status", status);
        this.orderColumn = "parent_id";
        this.orderDirection = "descending";
        return queryWrapper;
    }
}
