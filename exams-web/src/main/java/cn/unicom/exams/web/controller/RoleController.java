package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysRole;
import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.model.vo.RoleInfo;
import cn.unicom.exams.model.vo.UserVo;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysRoleService;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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

    @Autowired
    private ButtonAuthorUtils  buttonAuthorUtils;

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

    @GetMapping("/roleList")
    public String roleList(){
        return "role/rolelist";
    }

    @GetMapping("/getRoleList")
    @ResponseBody
    public WebResponse getRoleList(int page, int limit, SysRole role){
        try {
            QueryWrapper<SysRole> queryWrapper=new QueryWrapper<>();
            queryWrapper.likeRight(StringUtils.isNotEmpty(role.getName()),"name",role.getName());
            List<SysRole> sysRoles = sysRoleService.list(queryWrapper);
            WebResponse webResponse=new WebResponse();
            webResponse.setCode(0);
            webResponse.setMsg("");
            webResponse.setCount(sysRoles.size());
            ButtonInfo rolepower = buttonAuthorUtils.getButtonAuthority("role");
            List<RoleInfo> roleInfoList = new ArrayList<>();
            for (SysRole r : sysRoles) {
                RoleInfo info=new RoleInfo();
                info.setId(r.getId());
                info.setName(r.getName());
                info.setRemark(r.getRemark());
                info.setIsenable(r.getIsenable());
                info.setCreatetime(r.getCreatetime());
                info.setIsSetPower(rolepower.getIsSetPower());
                info.setIsDel(rolepower.getIsDel());
                info.setIsEdit(rolepower.getIsEdit());
                roleInfoList.add(info);
            }
            webResponse.setData(roleInfoList);
            return webResponse;
        }catch (Exception e){
            return new WebResponse(500,"系统错误",0);
        }


    }

    @GetMapping("/editRoleList")
    public String editRoleList(){
        return "role/roleAdd";
    }

    @GetMapping("/addRoleList")
    public String addRoleList(){
        return "role/roleAdd";
    }

}
