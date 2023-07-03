package abin.controller;

import abin.entity.Employee;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import abin.common.R;
import abin.entity.User;
import abin.service.UserService;
import abin.common.ValidateCodeUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("aBin","",phone,code);

            //需要将生成的验证码保存到Session
            session.setAttribute(phone,code);

            return R.success("手机验证码短信发送成功");
        }

        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
//        Object codeInSession = session.getAttribute(phone);
        Object codeInSession = "1111";

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession != null && codeInSession.equals(code)){
            //如果能够比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                Random random = new Random();
                int i = random.nextInt(10000);
                String name = i+"";
                user.setName("用户_"+name);
                user.setSex("2");
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

    /**
     * 移动端用户退出
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        //清理Session中保存的当前用户登录的id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }

    /**
     * 根据id修改用户信息
     * @param user
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody User user){
        log.info(user.toString());
        Long userId = (Long)request.getSession().getAttribute("user");

        log.info(userId.toString());
        userService.updateById(user);
        return R.success("用户信息修改成功");
    }


    /**
     * 用户信息查询
     * @param id
     * @return
     */
    @GetMapping()
    public R<User> page(HttpServletRequest request,long id){
        //添加过滤条件
        Long userId = (Long)request.getSession().getAttribute("user");
        if (userId == id){
            User user = userService.getById(id);
            return R.success(user);
        }
        //执行查询
        return R.error("noLogin");
    }

}
