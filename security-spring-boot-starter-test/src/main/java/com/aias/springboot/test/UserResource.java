package com.aias.springboot.test;

import com.aias.springboot.security.jwt.JwtTokenUtil;
import com.aias.springboot.security.jwt.JwtUser;
import com.aias.springboot.security.jwt.JwtUserFactory;
import com.aias.springboot.security.utils.StringUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/")
public class UserResource {
    private Logger logger = LoggerFactory.getLogger(UserResource.class);
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String CAR_2_FINANCE_USER = "CAR_2_FINANCE_USER";

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
    }

    public HttpServletResponse getResponse() {
        return ((ServletWebRequest) RequestContextHolder.getRequestAttributes())
                .getResponse();
    }

    /**
     * 获取手机验证码
     */
    @RequestMapping(value = "/shortMessage", method = {RequestMethod.POST})
    public ResponseEntity<Map<String, String>> shortMessage(String phone) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            logger.info("shortMessage phone={} ", phone);
            if (phone == null || "".equals(phone)) {
                logger.info("shortMessage手机号为空");
                return ResponseUtil.badResponse(HeaderUtil.createFailureAlert(
                        "user", "参数不合法", "手机号不能为空"), HttpStatus.BAD_REQUEST);
            }
            // 根据手机号查询用户
            User user = userService.queryByName(phone);
            if (user == null) {
                // 用户不存在
                logger.info("用户不存在phone：" + phone);
                return ResponseUtil.badResponse(
                        HeaderUtil.createFailureAlert("user", "用户不存在", "用户不存在"),
                        HttpStatus.BAD_REQUEST);
            }
            // 生成验证码/login.html
            String code = RandomStringUtils.randomNumeric(4);
            // 获取验证码
            String message = String.format(Constant.VERIFYING_CODE_TEMPLATE,
                    code);
            logger.info(message);
            // 短信是否开启
            code = "0000";
            result.put("message", "验证成功");
            result.put("status", "200");
            // 将验证码放入到redis中
            stringRedisTemplate.opsForValue().set(
                    CAR_2_FINANCE_USER + "_" + phone, code, 30,
                    TimeUnit.MINUTES);
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createAlert("短信发送成功", "phone"))
                    .body(result);
        } catch (Exception e) {
            logger.error("/appUser/shortMessage 调用异常", e);
            return ResponseUtil.badResponse(
                    HeaderUtil.createFailureAlert("user", "调用异常", "调用接口异常"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 登录
     *
     * @param phone
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ResponseEntity<Map<String, String>> login(String phone,
                                                     String password) {
        Map<String, String> result = new HashMap<String, String>();
        if (StringUtil.checkArgNull(phone, password)) {
            logger.info("/login手机号为空");
            return ResponseUtil.badResponse(
                    HeaderUtil.createFailureAlert("user", "参数不合法", "手机号不能为空"),
                    HttpStatus.BAD_REQUEST);
        }

        // 从缓存中获取手机验证码
        String sCode = stringRedisTemplate.opsForValue()
                .get(CAR_2_FINANCE_USER + "_" + phone);
        if (!password.equals(sCode)) {
            // 验证码校验失败
            logger.info("验证码校验失败phone：" + phone + " code:" + password
                    + " sCode:" + sCode);
            return ResponseUtil.badResponse(
                    HeaderUtil.createFailureAlert("user", "验证码校验失败", "验证码校验失败"),
                    HttpStatus.BAD_REQUEST);
        }
        // 根据手机号查询用户
        User user = userService.queryByName(phone);
        if (user == null) {
            // 用户不存在
            logger.info("用户不存在phone：" + phone);
            return ResponseUtil.badResponse(
                    HeaderUtil.createFailureAlert("user", "用户不存在", "用户不存在"),
                    HttpStatus.BAD_REQUEST);
        }

        result.put("message", "验证成功");
        result.put("status", "200");
        JwtUser userDetails = JwtUserFactory.create(String.valueOf(user.getId()), user.getPhone(), user.getUsername(), Arrays.asList(String.valueOf(user.getRole())));
        result.put("token", jwtTokenUtil.generateToken(userDetails));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(getRequest()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("验证成功", "200")).body(result);
    }

}
