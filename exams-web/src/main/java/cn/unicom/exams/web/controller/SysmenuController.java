package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysMenu;
import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.model.vo.MenuInfo;
import cn.unicom.exams.service.service.ISysMenuService;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 王长何
 * @create 2020-01-16 16:24
 */
@Controller
@RequestMapping("/sysmenu")
public class SysmenuController {
    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ButtonAuthorUtils buttonAuthorUtils;

    @GetMapping("/btnAuthroInfo")
    @ResponseBody
    public ButtonInfo btnAuthroInfo(String btn) throws Exception{
        try{
            ButtonInfo info = buttonAuthorUtils.getButtonAuthority(btn);
            return info;
        }catch (Exception e){
            throw new Exception("提取按钮权限错误："+e.getMessage());
        }

    }

    @GetMapping("/getAllMenuInfo/{roleId}")
    @ResponseBody
    public List<MenuInfo> getAllMenuInfo(@PathVariable("roleId") Long roleId) throws Exception{
        try{
            List<MenuInfo> allMenuInfo = sysMenuService.getAllMenuInfo();
            List<SysMenu> menus = sysMenuService.getSysMenuByRoleId(roleId);
            List<Long> list = menus.stream().map(SysMenu::getId).collect(Collectors.toList());
            List<MenuInfo> infos = getMenuInfoByChecked(allMenuInfo, list);
            return infos;
        }catch(Exception e){
            throw new Exception("提取系统菜单错误："+e.getMessage());
        }
    }

    private List<MenuInfo> getMenuInfoByChecked(List<MenuInfo> menus,List<Long> checkList){
        for(int i=0;i<menus.size();i++){
            if(menus.get(i).getChildren()!=null){
                setCheckBoxStatus(menus.get(i).getChildren(), checkList);
                getMenuInfoByChecked(menus.get(i).getChildren(), checkList);
            }

        }
        return menus;
    }
    private List<MenuInfo> setCheckBoxStatus(List<MenuInfo> infos,List<Long> checks){
        for(int i=0;i<infos.size();i++){
            for(Long id:checks){
                if(infos.get(i).getId()==id){
                    infos.get(i).setChecked(true);
                }
            }
        }
        return infos;
    }

}
