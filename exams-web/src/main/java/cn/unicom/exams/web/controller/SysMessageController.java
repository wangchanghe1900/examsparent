package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysUsermessages;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.vo.UserMessageInfo;
import cn.unicom.exams.model.vo.UserMessageVo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.model.web.WebResponse;
import cn.unicom.exams.service.service.ISysUsermessagesService;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 王长何
 * @create 2020-03-23 17:59
 */
@Controller
@RequestMapping("/message")
@Slf4j
public class SysMessageController {
    @Autowired
    private ISysUsermessagesService usermessagesService;

    @GetMapping("/messageList")
    @RequiresPermissions("message:list")
    public String messageList(){
        return "notice/messageList";
    }

    @GetMapping("/getMessageList")
    @RequiresPermissions("message:find")
    @ResponseBody
    public WebResponse getMessageList(int page, int limit, String data){
        try{
            if(data==null){
                data="";
            }
            data="["+data+"]";
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            List<UserMessageVo> userMessageVos = JSON.parseArray(data, UserMessageVo.class);
            IPage<UserMessageInfo> messagePage=null;
            UserMessageVo userMessageVo=new UserMessageVo();
            if(userMessageVos!=null && userMessageVos.size()>0){
                userMessageVo = userMessageVos.get(0);
            }
            userMessageVo.setUserCode(user.getId().toString());
            userMessageVo.setUserName(user.getUsername());
            messagePage = usermessagesService.getUserMessageInfoByConditon(page, limit, userMessageVo);
            Long count=messagePage.getTotal();
            return new WebResponse(0,"",count.intValue(),messagePage.getRecords());
        }catch(Exception e){
            log.error(e.getMessage());
            return new WebResponse(500,"提取数据失败",0);
        }

    }

    @GetMapping("/showDetailList")
    public String showDetailList(){
        return "notice/showMessage";
    }

    @GetMapping("/updateMessageStatus")
    @ResponseBody
    public Response updateMessageStatus(Long id,String isRead){
        try{
            SysUsermessages usermessages=new SysUsermessages();
            usermessages.setId(id);
            usermessages.setIsRead(isRead);
            usermessagesService.updateById(usermessages);
            return new Response(200,"更新数据成功!");
        }catch (Exception e){
            return new Response(500,"更新数据失败!");
        }
    }

    @GetMapping("/getUserMessageCount")
    @ResponseBody
    public Response getUserMessageCount(String userName){
        try{
            Integer userMessageCount = usermessagesService.getUserMessageCount(userName);
            return new Response(200,"",userMessageCount);
        }catch (Exception e){
            log.error(e.getMessage());
            return new Response(500,"提取消息错误");
        }
    }
}
