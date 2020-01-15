package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysMenu;
import cn.unicom.exams.model.entity.SysUser;
import cn.unicom.exams.model.vo.NavsMenuInfo;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.vo.UserVo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysMenuService;
import cn.unicom.exams.service.service.ISysUserService;

import cn.unicom.exams.web.utils.MD5Utils;
import cn.unicom.exams.web.utils.SecurityCode;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
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
        IPage<UserInfo> sysUserByPage = userService.getSysUserByPage(page, limit, userVo);
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
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            if(!username.equals(user.getUsername())){
                throw new Exception("用户名错误");
            }
            List<SysMenu> sysMenuList = sysMenuService.getTopSysMenuByName(username);
            return sysMenuList;
        }catch(Exception e){
            throw new Exception("获取URL错误："+e.getMessage());
        }
    }

    @GetMapping("getAllNavsMenu")
    @ResponseBody
    public List<NavsMenuInfo> getAllNavsMenu(String username) throws Exception{
        try{
            List<NavsMenuInfo> allNavsMenu = sysMenuService.getAllNavsMenu(username);
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
        //System.out.println("userids = " + userids);
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
    public Response addUser(UserVo userVo){
        try {
            if (userVo.getId() != null) {
                Response response = userService.updateUser(userVo);
                return response;
            } else {
                String salt = SecurityCode.getSecurityCode();
                String pwd = MD5Utils.getAuthenticationInfo("Abcd#123!", salt);//初始密码：Abcd#123!
                userVo.setPassword(pwd);
                userVo.setSalt(salt);
                Response response = userService.addUser(userVo);
                return response;
            }
        }catch (Exception e){
            return new Response(500,e.getMessage());
        }

    }

    @PostMapping("/getUserInfo/{username}")
    @ResponseBody
    public SysUser getUserInfo(@PathVariable("username") String username){
        QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        SysUser user = userService.getOne(queryWrapper);
        return user;
    }

    @PostMapping("/resetpwd/{id}")
    @ResponseBody
    public String resetpwd(@PathVariable("id") Long id){
        QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id);
        SysUser user = userService.getOne(queryWrapper);
        String salt = user.getSalt();
        String pwd = MD5Utils.getAuthenticationInfo("Abcd#123!", salt);
        SysUser sysUser=new SysUser();
        sysUser.setId(id);
        sysUser.setPassword(pwd);
        userService.updateById(sysUser);
        return "success";
    }

    @GetMapping("/changePwd")
    public String changePwd(){
        return "user/changePwd";
    }

    @PostMapping("/verifyPwd")
    @ResponseBody
    public String verifyPwd(String username,String password){
        QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        SysUser sysUser = userService.getOne(queryWrapper);
        if(sysUser==null){
            return "false";
        }
        String salt = sysUser.getSalt();
        String pwd = MD5Utils.getAuthenticationInfo(password, salt);
        if(pwd.equals(sysUser.getPassword())){
            return "success";
        }
        return "false";
    }

    @PostMapping("/updatePwd")
    @ResponseBody
    public Response updatePwd(String username,String oldPwd,String newPass){
        QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        SysUser sysUser = userService.getOne(queryWrapper);
        if(sysUser==null){
            return new Response(500,"用户不存在！");
        }
        String salt = sysUser.getSalt();
        String pwd = MD5Utils.getAuthenticationInfo(oldPwd, salt);
        if(!pwd.equals(sysUser.getPassword())){
            return  new Response(500,"旧密码不正确！");
        }
        UpdateWrapper<SysUser> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("username",username);
        String newpass = MD5Utils.getAuthenticationInfo(newPass, salt);
        SysUser user=new SysUser();
        user.setPassword(newpass);
        try {
            boolean update = userService.update(user, updateWrapper);
            return  new Response(200,"密码更新成功！");
        }catch (Exception e){
            return  new Response(500,"密码更新失败！");
        }

    }

}
