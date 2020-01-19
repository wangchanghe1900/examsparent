package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.model.vo.MenuInfo;
import cn.unicom.exams.service.service.ISysMenuService;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    @GetMapping("/getAllMenuInfo")
    @ResponseBody
    public List<MenuInfo> getAllMenuInfo() throws Exception{
        try{
            List<MenuInfo> allMenuInfo = sysMenuService.getAllMenuInfo();
            return allMenuInfo;
        }catch(Exception e){
            throw new Exception("提取系统菜单错误："+e.getMessage());
        }
    }

}
