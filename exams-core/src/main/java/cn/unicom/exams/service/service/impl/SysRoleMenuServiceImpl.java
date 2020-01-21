package cn.unicom.exams.service.service.impl;

import cn.unicom.exams.model.entity.SysRoleMenu;
import cn.unicom.exams.model.vo.MenuInfo;
import cn.unicom.exams.service.mapper.SysRoleMenuMapper;
import cn.unicom.exams.service.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色与菜单对应关系 服务实现类
 * </p>
 *
 * @author 王长河
 * @since 2019-12-31
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRoleAndPermiss(Long roleId, List<MenuInfo> infos) {
        try{
            QueryWrapper<SysRoleMenu> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("role_id",roleId);
            sysRoleMenuMapper.delete(queryWrapper);
            List<Long> menuId = getMenuId(infos);
            for(Long id: menuId){
                SysRoleMenu roleMenu=new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(id);
                sysRoleMenuMapper.insert(roleMenu);
            }
            return true;
        }catch(Exception e){
            System.out.println("更新角色权限错误："+e.getMessage());
        }
        return false;
    }
    
    private List<Long> getMenuId(List<MenuInfo> infos){
        List<Long> list=new ArrayList<>();
        for(MenuInfo info:infos){
            Long id = info.getId();
            list.add(id);
            if(info.getChildren()!=null){
               List<Long> chilList=getMenuId(info.getChildren());
               list.addAll(chilList);
            }
        }
        return list;
    }
}
