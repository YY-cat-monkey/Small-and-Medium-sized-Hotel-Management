package com.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.entity.SysUser;
import com.hotel.mapper.SysUserMapper;
import com.hotel.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 系统用户服务实现类
 * 继承ServiceImpl并实现SysUserService接口，提供系统用户相关的业务逻辑实现
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    /**
     * 用户登录方法
     * @param username 用户名
     * @param password 密码
     * @return SysUser 登录成功返回用户信息，失败返回null
     */
    @Override
    public SysUser login(String username, String password) {
        // 根据用户名查询用户信息
        SysUser user = baseMapper.selectByUsername(username);
        // 如果用户不存在，返回null
        if (user == null) {
            return null;
        }
        // 将输入的密码进行MD5加密
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        // 比较加密后的密码与数据库中存储的密码是否一致
        if (!md5.equals(user.getPassword())) {
            return null;
        }
        // 检查用户状态是否有效（1表示有效，0表示无效）
        if (user.getStatus() == null || user.getStatus() == 0) {
            return null;
        }
        // 所有验证通过，返回用户信息
        return user;
    }
}
