package cn.unicom.exams.service.service;

import cn.unicom.exams.model.entity.SysRoleMenu;
import cn.unicom.exams.model.vo.MenuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色与菜单对应关系 服务类
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {
    /**
     * 保存角色权限信息
     * @param roleId
     * @param infos
     * @return
     */
    public Boolean saveRoleAndPermiss(Long roleId, List<MenuInfo> infos);
}
