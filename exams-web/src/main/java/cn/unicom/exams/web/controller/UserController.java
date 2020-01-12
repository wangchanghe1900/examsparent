package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysMenu;
import cn.unicom.exams.model.vo.NavsMenuInfo;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.vo.UserVo;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysMenuService;
import cn.unicom.exams.service.service.ISysUserService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 王长何
 * @create 2019-12-23 16:30
 */
@Controller
public class UserController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysMenuService sysMenuService;

    @GetMapping("/userList")
    public String userList()throws Exception{
        return "user/userList";
    }

    @GetMapping("/getUserList")
    @ResponseBody
    @RequiresPermissions("userlist:list")
    public WebResponse getUserList(int page, int limit, UserVo userVo){
        IPage<UserInfo> sysUserByPage = userService.getUserInfoByPage(page, limit, userVo);
        WebResponse userResponse=new WebResponse();
        if(sysUserByPage != null){
            userResponse.setCode(0);
            Long total=sysUserByPage.getTotal();
            userResponse.setCount(total.intValue());
            userResponse.setData(sysUserByPage.getRecords());
        }
        return userResponse;
    }

    @PostMapping("/getURLByName/{username}")
    @ResponseBody
    public List<SysMenu> getURLByName(@PathVariable("username") String username) throws Exception{
        try{
            List<SysMenu> sysMenuList = sysMenuService.getTopSysMenuByName(username);
            return sysMenuList;
        }catch(Exception e){
            throw new Exception("获取URL错误："+e.getMessage());
        }
    }

    @GetMapping("getAllNavsMenu")
    @ResponseBody
    public List<NavsMenuInfo> getAllNavsMenu() throws Exception{
        try{
            List<NavsMenuInfo> allNavsMenu = sysMenuService.getAllNavsMenu();
            return allNavsMenu;
        }catch (Exception e){
            throw new Exception("获取导航菜单错误："+e.getMessage());
        }

    }

    @GetMapping("/addUserList")
    public String addUserList(){
        return "/user/userAdd";
    }

    @GetMapping("delUserById")
    @ResponseBody
    public Boolean delUserById(Integer id){
        boolean b = userService.removeById(id);
        return b;
    }

    @PostMapping("delUserByIds")
    @ResponseBody
    public Boolean delUserByIds(String userids){
        System.out.println("userids = " + userids);
        String[] arr=userids.split(",");
        List<Integer> ids=new ArrayList<>();
        for(String s: arr){
            ids.add(Integer.valueOf(s));
        }
        boolean b = userService.removeByIds(ids);
        return b;

    }
    @PostMapping("/addUser")
    @ResponseBody
    public Boolean addUser(UserVo userVo){

        return false;
    }

}
