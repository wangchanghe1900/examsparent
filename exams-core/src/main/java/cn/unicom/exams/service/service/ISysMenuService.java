package cn.unicom.exams.service.service;

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

}
