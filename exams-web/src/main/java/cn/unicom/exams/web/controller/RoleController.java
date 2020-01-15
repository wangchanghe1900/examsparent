package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysRole;
import cn.unicom.exams.model.vo.RoleInfo;
import cn.unicom.exams.model.vo.UserVo;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 王长何
 * @create 2020-01-14 10:36
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private ISysRoleService sysRoleService;

    @GetMapping("/getRoleInfo")
    @ResponseBody
    public WebResponse getRoleInfo(){
        QueryWrapper<SysRole> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("isenable",1);
        List<SysRole> sysRoles = sysRoleService.list(queryWrapper);

        if(sysRoles!=null){
            List<RoleInfo> roleList=new ArrayList<>();
            for(SysRole role:sysRoles){
                RoleInfo info=new RoleInfo();
                info.setName(role.getName());
                info.setValue(role.getId().intValue());
                info.setDisabled("");
                info.setSelected("");
                roleList.add(info);
            }
            return new WebResponse(0,"success",roleList.size(),roleList);
        }else {
            return new WebResponse(500,"false",0);
        }
    }

    @GetMapping("/rolelist")
    public String rolelist(){
        return "role/rolelist";
    }

    @GetMapping("/getRoleList")
    @ResponseBody
    public WebResponse getUserList(int page, int limit, SysRole role){

        return null;
    }

}
