package cn.unicom.exams.service.service;

import cn.unicom.exams.model.vo.MenuInfo;
import cn.unicom.exams.model.vo.NavsMenuInfo;
import cn.unicom.exams.model.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 根据用户名查询首页菜单
     * @param username
     * @return
     */
    public List<SysMenu> getTopSysMenuByName(String username) throws Exception;

    /**
     * 提取所以菜单权限
     * @return
     */
    public List<String> getAllSysMenuList() ;


    /**
     * 提取导航菜单
     * @return
     */
    public List<NavsMenuInfo> getAllNavsMenu(String username) throws Exception;

    /**
     * 根据用户名、按钮名提取权限
     * @param username
     * @param buttonstr
     * @return
     * @throws Exception
     */
    public List<SysMenu> getButtonMenu(String username,String buttonstr) throws Exception;

    /**
     * 提取所以菜单及子菜单
     * @return
     * @throws Exception
     */
    public List<MenuInfo> getAllMenuInfo() throws Exception;

    /**
     * 根据角色ID查询角色所以权限信息
     * @param roleId
     * @return
     * @throws Exception
     */
    public List<SysMenu> getSysMenuByRoleId(Long roleId) throws Exception;

}
