package com.hotel.controller;

import com.hotel.entity.SysUser;
import com.hotel.service.SysUserService;
import com.hotel.vo.Result;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 用户控制器
 * 处理用户相关的HTTP请求，提供用户数据的增删改查接口
 */
@RestController
@RequestMapping("/api/user")
public class SysUserController {

    /**
     * 用户服务接口，用于处理用户相关的业务逻辑
     */
    private final SysUserService sysUserService;

    /**
     * 构造函数，通过依赖注入方式获取SysUserService实例
     * @param sysUserService 用户服务接口实例
     */
    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 获取用户列表接口
     * @return 返回用户列表数据，封装在Result中
     */
    @GetMapping("/list")
    public Result<List<SysUser>> list() {
        return Result.success(sysUserService.list());
    }

    /**
     * 保存或更新用户信息接口
     * @param user 用户信息实体
     * @return 返回操作结果，封装在Result中
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody SysUser user) {
        // 如果密码不为空且长度小于32，则进行MD5加密
        if (user.getPassword() != null && !user.getPassword().isEmpty() && user.getPassword().length() < 32) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        } else if (user.getPassword() != null && user.getPassword().isEmpty()) {
            user.setPassword(null);
        }
        return Result.success(sysUserService.saveOrUpdate(user));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(sysUserService.removeById(id));
    }
}
