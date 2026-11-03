package com.hotel.controller;

import com.hotel.entity.SysUser;
import com.hotel.service.SysUserService;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器，处理用户登录、登出等相关请求
 */
@Controller
public class LoginController {

    /**
     * 系统用户服务，用于处理用户相关的业务逻辑
     */
    private final SysUserService sysUserService;

    /**
     * 构造函数，通过依赖注入方式获取SysUserService实例
     * @param sysUserService 系统用户服务实例
     */
    public LoginController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 处理登录页面GET请求，返回登录页面视图
     * @return 登录页面视图名称
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 处理用户登录POST请求
     * @param username 用户名
     * @param password 密码
     * @param session HTTP会话对象
     * @return 包含登录结果的Map对象，包含状态码、消息和用户角色信息
     */
    @PostMapping("/doLogin")
    @ResponseBody
    public Map<String, Object> doLogin(@RequestParam String username,
                                       @RequestParam String password,
                                       HttpSession session) {
        // 创建结果Map
        Map<String, Object> result = new HashMap<>();
        // 调用服务层方法验证用户登录
        SysUser user = sysUserService.login(username, password);
        if (user == null) {
            // 登录失败，设置错误信息
            result.put("code", 500);
            result.put("msg", "用户名或密码错误");
            return result;
        }
        // 登录成功，将用户信息存入会话
        session.setAttribute("user", user);
        // 设置成功响应信息
        result.put("code", 200);
        result.put("msg", "登录成功");
        result.put("role", user.getRole());
        return result;
    }

    /**
     * 处理用户登出请求
     * @param session HTTP会话对象
     * @重定向到登录页面
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 使会话失效，清除用户信息
        session.invalidate();
        // 重定向到登录页面
        return "redirect:/login";
    }
}
