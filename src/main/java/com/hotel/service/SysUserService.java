package com.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    SysUser login(String username, String password);
}
