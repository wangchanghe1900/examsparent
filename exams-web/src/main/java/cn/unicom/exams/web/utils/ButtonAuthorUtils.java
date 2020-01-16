package cn.unicom.exams.web.utils;

import cn.unicom.exams.model.entity.SysMenu;
import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.service.service.ISysMenuService;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 王长何
 * @create 2020-01-16 16:00
 */
@Component
public class ButtonAuthorUtils {
    @Autowired
    private ISysMenuService sysMenuService;

    public ButtonInfo getButtonAuthority(String buttonstr) throws Exception{
        Subject subject = ShiroUtils.getSubject();
        UserInfo user = (UserInfo) subject.getPrincipal();
        try{
            List<SysMenu> buttonMenu = sysMenuService.getButtonMenu(user.getUsername(), buttonstr);
            ButtonInfo bi=new ButtonInfo();
            for(SysMenu s:buttonMenu){
                if(s.getPerms().contains("add")){
                    bi.setIsAdd(true);
                }
                if(s.getPerms().contains("edit")){
                    bi.setIsEdit(true);
                }
                if(s.getPerms().contains("update")){
                    bi.setIsUpdate(true);
                }
                if(s.getPerms().contains("delete")){
                    bi.setIsDel(true);
                }
                if(s.getPerms().contains("resetPwd")){
                    bi.setIsResetPwd(true);
                }
                if(s.getPerms().contains("find")){
                    bi.setIsFind(true);
                }
                if(s.getPerms().contains("batchdel")){
                    bi.setIsBatchDel(true);
                }
            }
            return bi;
        }catch(Exception e){
            throw new Exception("获取权限按钮错误："+e.getMessage());
        }
    }
}
