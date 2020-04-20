package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysLoginlog;
import cn.unicom.exams.model.entity.SysUser;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.web.Response;
import cn.unicom.exams.service.service.ISysLoginlogService;
import cn.unicom.exams.service.service.ISysUserService;
import cn.unicom.exams.web.utils.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author 王长河
 * @create 2019-12-09 16:20
 */
@Controller
public class LoginController {
    @Autowired
    private Producer producer;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysLoginlogService sysLoginlogService;


    @GetMapping("/")
    public String index(){
        return "login/login";
    }

    @GetMapping("/index")
    public String main(){
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            if(user==null){
                return "redirect:/";
            }else{
                if(user.getUsername()==null){
                    return "redirect:/";
                }
                return "index";
            }

    }

    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletRequest request, HttpServletResponse response)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        //HttpSession session = request.getSession();
        //session.setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }

    @PostMapping("/login")
    @ResponseBody
    public Response login(HttpServletRequest request, String username, String password, String code){

        try{
            String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
            if(!code.equalsIgnoreCase(kaptcha)){
                return new Response(500,"验证码错误！");
            }
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            SysUser user=new SysUser();
            user.setLastlogintime(LocalDateTime.now());
            QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("username",username);
            sysUserService.update(user,queryWrapper);
            SysLoginlog log=new SysLoginlog();
            log.setUserCode(username);
            log.setRequestPath(request.getRequestURI());
            log.setRequestAddress(request.getRemoteAddr());
            log.setLoginDateTime(LocalDateTime.now());
            log.setLoginStatus("成功");
            sysLoginlogService.save(log);
        }catch (UnknownAccountException e) {
            return new Response(500,e.getMessage());
        }catch (IncorrectCredentialsException e) {
            return new Response(500,"账号或密码不正确！");
        }catch (LockedAccountException e) {
            return new Response(500,"账号已被锁定,请联系管理员");
        }catch (AuthenticationException e) {
            return new Response(500,"账户验证失败");
        }catch(Exception e){
            return new Response(500,e.getMessage());
        }

        return new Response(200,"验证通过",username);

    }
   @GetMapping("/main")
    public String main1(){
        return "main";
   }

    @RequestMapping("/403")
    public String unauthorizedRole(){
        return "403";
    }

    /**
     * 退出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        ShiroUtils.logout();
        return "redirect:/";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error() {
        return "error";
    }

    @GetMapping("/list404")
    public String list404(){
        return "404";
    }

}
