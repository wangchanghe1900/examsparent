package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysRole;
import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.model.vo.MenuInfo;
import cn.unicom.exams.model.vo.RoleInfo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysMenuService;
import cn.unicom.exams.service.service.ISysRoleMenuService;
import cn.unicom.exams.service.service.ISysRoleService;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    ISysRoleMenuService sysRoleMenuService;

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
    @RequiresPermissions("role:list")
    public String roleList(){
        return "role/rolelist";
    }

    @GetMapping("/getRoleList")
    @RequiresPermissions("role:find")
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
    @RequiresPermissions("role:edit")
    public String editRoleList(){
        return "role/roleAdd";
    }

    @GetMapping("/addRoleList")
    @RequiresPermissions("role:add")
    public String addRoleList(){
        return "role/roleAdd";
    }

    @PostMapping("/addRole")
    @ResponseBody
    public Response addRole(SysRole sysRole){
        try {
            if (sysRole.getId() != null) {
                boolean b = sysRoleService.updateById(sysRole);
                if(b){
                    return new Response(200,"角色更新成功");
                }else{
                    return new Response(500,"角色更新失败");
                }

            } else {
                boolean b = sysRoleService.save(sysRole);
                if(b){
                    return new Response(200,"角色新增成功");
                }else{
                    return new Response(500,"角色新增失败");
                }
            }
        }catch (Exception e){
            return new Response(500,e.getMessage());
        }

    }

    @PostMapping("/delRoleByIds")
    @RequiresPermissions("role:batchdel")
    @ResponseBody
    public Boolean delRoleByIds(String roleIds){
        String[] arr=roleIds.split(",");
        List<Integer> ids=new ArrayList<>();
        for(String s: arr){
            ids.add(Integer.valueOf(s));
        }
        boolean b = sysRoleService.removeByIds(ids);
        return b;
    }

    @GetMapping("/delRoleById")
    @RequiresPermissions("role:delete")
    @ResponseBody
    public Boolean delRoleById(Long id){
        if(id!=null){
            boolean b = sysRoleService.removeById(id);
            return b;
        }
        return false;
    }

    @GetMapping("/setPermissList")
    @RequiresPermissions("role:setpower")
    public String setPermissList(){
        return "role/permissSet";
    }


    @PostMapping("/savePermiss")
    @ResponseBody
    public Response savePermiss(Long  roleId,String infos){
        List<MenuInfo> menuInfos = JSON.parseArray(infos, MenuInfo.class);
        Boolean aBoolean = sysRoleMenuService.saveRoleAndPermiss(roleId, menuInfos);
        if(aBoolean){
            return new Response(200,"保存成功");
        }else{
            return new Response(500,"保存失败");
        }

    }


}
