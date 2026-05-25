package com.pickle.sys.service.impl;

import com.pickle.sys.bean.SysUser;
import com.pickle.sys.mapper.SysUserMapper;
import com.pickle.sys.service.ISysUserService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SysUserService extends BaseService<SysUser> implements ISysUserService {
    private final SysUserMapper sysUserMapper;
    private final JwtUtil jwtUtil;

    @Override
    public SysUser login(SysUser sysUser) {
        // 创建一个新的 SysUser 对象，查找用户
        SysUser userBean = new SysUser();
        userBean.setUserName(sysUser.getUserName());
        List<SysUser> userList = sysUserMapper.selectListByBean(userBean);
        // 判断用户是否存在
        if (userList.isEmpty()){
            throw new BizException("用户名不存在");
        }

        SysUser user = userList.get(0);
        // 用户存在，验证密码
        if (!user.getUserPassword().equals(sysUser.getUserPassword())) {
            throw new BizException("密码错误");
        }

        log.info("token开始生成");
        // 密码正确，返回成功，生成一个token
        String token = jwtUtil.generateToken(user.getUserUuid(), user.getUserPassword());
        user.setToken(token);
//        redisService.set(user.getUserUuid(), user, 60 * 30);
        return user;
    }

    @Override
    public List<SysUser> queryPageList(SysUser sysUser) {
        return sysUserMapper.selectListByBean((sysUser));
    }
}