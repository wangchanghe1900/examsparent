package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.vo.ButtonInfo;
import cn.unicom.exams.web.utils.ButtonAuthorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 王长何
 * @create 2020-01-16 16:24
 */
@Controller
@RequestMapping("/sysmenu")
public class SysmenuController {
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

}
