package cn.unicom.exams.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.unicom.exams.model.entity.SysDept;
import cn.unicom.exams.model.entity.SysMenu;
import cn.unicom.exams.model.entity.SysRole;
import cn.unicom.exams.model.entity.SysUser;
import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.model.vo.NavsMenuInfo;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.vo.UserVo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysDeptService;
import cn.unicom.exams.service.service.ISysMenuService;
import cn.unicom.exams.service.service.ISysRoleService;
import cn.unicom.exams.service.service.ISysUserService;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import cn.unicom.exams.web.utils.MD5Utils;
import cn.unicom.exams.web.utils.SecurityCode;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 王长何
 * @create 2019-12-23 16:30
 */
@Controller
@Slf4j
public class UserController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ButtonAuthorUtils buttonAuthorUtils;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;


    @GetMapping("/userList")
    @RequiresPermissions("user:list")
    public String userList()throws Exception{
        return "user/userList";
    }

    @GetMapping("/getUserList")
    @RequiresPermissions("user:find")
    @ResponseBody
    public WebResponse getUserList(int page, int limit, UserVo userVo){
        WebResponse userResponse=new WebResponse(0,"",0);
        try{
            IPage<UserInfo> sysUserByPage = userService.getSysUserByPage(page, limit, userVo);
            ButtonInfo userbutton = buttonAuthorUtils.getButtonAuthority("user");
            if(sysUserByPage != null){
                userResponse.setCode(0);
                Long total=sysUserByPage.getTotal();
                userResponse.setCount(total.intValue());
                List<UserInfo> records = sysUserByPage.getRecords();
                List<UserInfo> userList=new ArrayList<>();
                for(UserInfo info:records){
                    List<SysRole> roleList = roleService.getRoleInfoByUId(info.getId());
                    info.setRoles(roleList);
                    info.setIsAdd(userbutton.getIsAdd());
                    info.setIsDel(userbutton.getIsDel());
                    info.setIsUpdate(userbutton.getIsUpdate());
                    info.setIsEdit(userbutton.getIsEdit());
                    info.setIsResetPwd(userbutton.getIsResetPwd());
                    userList.add(info);
                }
                userResponse.setData(userList);
            }
        }catch(Exception e){
            return new WebResponse(500,"系统错误",0);
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
    @RequiresPermissions("user:add")
    public String addUserList(){
        return "/user/userAdd";
    }

    @GetMapping("/editUserList")
    @RequiresPermissions("user:edit")
    public String editUserList(){
        return "/user/userAdd";
    }

    @GetMapping("delUserById")
    @RequiresPermissions("user:delete")
    @ResponseBody
    public Boolean delUserById(Integer id){
        boolean b = userService.removeById(id);
        return b;
    }

    @PostMapping("delUserByIds")
    @RequiresPermissions("user:batchdel")
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
    public UserInfo getUserInfo(@PathVariable("username") String username) throws Exception{
        try{
            QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("username",username);
            SysUser user = userService.getOne(queryWrapper);
            UserInfo userInfo=new UserInfo();
            QueryWrapper<SysDept> qw=new QueryWrapper<>();
            qw.eq("id",user.getDeptId());
            SysDept sysDept = deptService.getOne(qw);
            BeanUtil.copyProperties(user,userInfo);
            userInfo.setDeptname(sysDept.getDeptname());
            return userInfo;
        }catch (Exception e){
            log.error("提取用户信息错误："+e.getMessage());
            throw new Exception("提取用户信息错误："+e.getMessage());
        }

    }

    @PostMapping("/resetpwd/{id}")
    @RequiresPermissions("user:resetPwd")
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
        try {
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
            String regex="^^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*_-]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*_-]+$)(?![\\d!@#$%^&*_-]+$)[a-zA-Z\\d!@#$%^&*_-]+$";
            if(oldPwd.equals(newPass)){
                return new Response(400,"新密码和旧密码一样");
            }
            if(StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPass)){
                return new Response(400,"密码为空");
            }
            if(newPass.length()<8){
                return new Response(400,"新密码需要8位以上");
            }
            if(!Pattern.matches(regex, newPass)){
                return new Response(400,"密码复杂度不符合要求");
            }
            UpdateWrapper<SysUser> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("username",username);
            String newpass = MD5Utils.getAuthenticationInfo(newPass, salt);
            SysUser user=new SysUser();
            user.setPassword(newpass);
            user.setLastmdpasstime(LocalDateTime.now());
            boolean update = userService.update(user, updateWrapper);
            return  new Response(200,"密码更新成功！");
        }catch (Exception e){
            return  new Response(500,"密码更新失败！");
        }

    }



}
