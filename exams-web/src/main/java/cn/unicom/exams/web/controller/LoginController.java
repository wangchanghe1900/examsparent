package cn.unicom.exams.web.controller;

import cn.unicom.exams.model.entity.SysUser;
import cn.unicom.exams.model.vo.UserInfo;
import cn.unicom.exams.model.web.Response;
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


    @GetMapping("/")
    public String index(){
        return "login/login";
    }

    @GetMapping("/index")
    public String main(String userName,String nowTime){
        //System.out.println("userName = " + userName + ", nowTime = " + nowTime);
        if(userName==null || "".equals(userName)){
            return "redirect:/";
        }else{
            Subject subject = ShiroUtils.getSubject();
            UserInfo user = (UserInfo) subject.getPrincipal();
            if(!userName.equals(user.getUsername())){
                return "redirect:/";
            }else{
                return "index";
            }
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
        HttpSession session = request.getSession();
        //session.setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }

    @PostMapping("/login")
    @ResponseBody
    public Response login(HttpServletRequest request, String username, String password, String code){
/*        HttpSession session = request.getSession();
        String sessioncode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);*/

 /*      if(!sessioncode.equals(code)){
            return new Response(500,"验证码错误！");
        }else{
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Auth-Token", "e348bc22-5efa-4299-9142-529f07a18ac9");
            MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
            postParameters.add("userName","admin");
            postParameters.add("password",password);
            HttpEntity<MultiValueMap<String, String>> requestEntity  = new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
            Response response = restTemplate.postForObject("http://127.0.0.1:9001/v1/getUserByName",requestEntity,Response.class);
            if(response!=null){
                return response;
            }*/
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

}
