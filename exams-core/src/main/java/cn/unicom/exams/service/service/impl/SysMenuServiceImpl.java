package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysMenu;
import cn.unicom.exams.model.vo.*;
import cn.unicom.exams.service.mapper.SysMenuMapper;
import cn.unicom.exams.service.service.ISysMenuService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Override
    public List<SysMenu> getTopSysMenuByName(String username) throws Exception{
        QueryWrapper<UserVo> queryWrapper=new QueryWrapper<>();
        if("admin".equalsIgnoreCase(username)){
            queryWrapper
                    .eq("f.parent_id", 0)
                    .orderByAsc("f.order_num");
        }else {
            queryWrapper.eq("a.username", username)
                    .eq("f.parent_id", 0)
                    .orderByAsc("f.order_num");
        }
        return sysMenuMapper.getTopSysmenuByName(queryWrapper);
    }

    @Override
    public List<String> getAllSysMenuList()  {
        Wrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        try{
            List<SysMenu> sysMenus = sysMenuMapper.selectList(queryWrapper);
            if(sysMenus!=null){
                List<String> stringList = sysMenus.stream().map(menu -> menu.getPerms()).collect(Collectors.toList());
                return stringList;
            }
        }catch (Exception e){
            System.out.println("sysMenuService异常："+e.getMessage());
        }

        return null;
    }
    private List<NavsMenuInfo> sysMenu2NavsInfo(Long parentId,List<SysMenu> menus){
        List<SysMenu> submenus = menus.stream().filter(menu -> menu.getParentId() == parentId).collect(Collectors.toList());
        List<NavsMenuInfo> navsMenuInfos=new ArrayList<>();
        for (int i = 0; i <submenus.size() ; i++) {
            NavsMenuInfo navsMenuInfo=new NavsMenuInfo();
            navsMenuInfo.setParentName(submenus.get(i).getName());
            Long pId=submenus.get(i).getId();
            navsMenuInfo.setNavsList(getNavsinfos(pId.intValue(),menus));
            navsMenuInfos.add(navsMenuInfo);

        }
        return  navsMenuInfos;
    }

    private List<NavsInfo> getNavsinfos(int pId,List<SysMenu> menus){
        List<SysMenu> submenus = menus.stream().filter(menu -> menu.getParentId() == pId).collect(Collectors.toList());
        List<NavsInfo> navsList=new ArrayList<>();
        for (SysMenu s:submenus) {
            NavsInfo navsInfo=new NavsInfo();
            navsInfo.setTitle(s.getName());
            navsInfo.setIcon(s.getIcon());
            navsInfo.setHref(s.getUrl());
            navsInfo.setSpread(false);
            navsInfo.setChildren(getNavsinfos(s.getId().intValue(),menus));
            navsList.add(navsInfo);
        }

        return navsList;
    }

    @Override
    public List<NavsMenuInfo> getAllNavsMenu(String username) throws Exception{
        QueryWrapper<UserVo> queryWrapper=new QueryWrapper<>();
        if("admin".equalsIgnoreCase(username)){
            QueryWrapper<SysMenu> queryWrapper1=new QueryWrapper<>();
            queryWrapper1.ne("type",2);
            queryWrapper1.orderByAsc("parent_id").orderByAsc("order_num");
            List<SysMenu> sysMenus = sysMenuMapper.selectList(queryWrapper1);
            return sysMenu2NavsInfo(0L,sysMenus);
        }else {
            queryWrapper.eq("a.username", username)
                    .ne("f.type",2)
                    .orderByAsc("f.parent_id")
                    .orderByAsc("f.order_num");
            List<SysMenu> sysMenulist = sysMenuMapper.getNavsByName(queryWrapper);
            return sysMenu2NavsInfo(0L,sysMenulist);
        }

    }

    @Override
    public List<SysMenu> getButtonMenu(String username, String buttonstr) throws Exception {
        QueryWrapper<UserVo> queryWrapper=new QueryWrapper<>();
        if("admin".equalsIgnoreCase(username)){
            QueryWrapper<SysMenu> queryWrapper1=new QueryWrapper<>();
            queryWrapper1.eq("type",2);
            queryWrapper1.orderByAsc("parent_id").orderByAsc("order_num");
            List<SysMenu> sysMenus = sysMenuMapper.selectList(queryWrapper1);
           return sysMenus.stream().filter(s -> s.getPerms().contains(buttonstr)).collect(Collectors.toList());
        }else {
            queryWrapper.eq("a.username", username)
                    .eq("f.type",2)
                    .orderByAsc("f.parent_id")
                    .orderByAsc("f.order_num");
            List<SysMenu> sysMenulist = sysMenuMapper.getNavsByName(queryWrapper);
            return sysMenulist.stream().filter(s -> s.getPerms().contains(buttonstr)).collect(Collectors.toList());
        }
    }

    @Override
    public List<MenuInfo> getAllMenuInfo() throws Exception {
        List<SysMenu> sysMenus = sysMenuMapper.selectList(null);
        return getsubMenuinfos(0,sysMenus);
    }

    private List<MenuInfo> getsubMenuinfos(int pId,List<SysMenu> menus){
        List<SysMenu> submenus = menus.stream().filter(menu -> menu.getParentId() == pId).collect(Collectors.toList());
        List<MenuInfo> menuList=new ArrayList<>();
        for (SysMenu s:submenus) {
            MenuInfo menuInfo=new MenuInfo();
            menuInfo.setId(s.getId());
            menuInfo.setTitle(s.getName());
            menuInfo.setChildren(getsubMenuinfos(s.getId().intValue(),menus));
            menuList.add(menuInfo);
        }

        return menuList;
    }
}
