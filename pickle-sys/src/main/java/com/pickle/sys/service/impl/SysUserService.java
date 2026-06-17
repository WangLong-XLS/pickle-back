package com.pickle.sys.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pickle.sys.bean.SysUser;
import com.pickle.sys.config.SecurityProperties;
import com.pickle.sys.mapper.SysUserMapper;
import com.pickle.sys.service.ISysUserService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.date.DateUtils;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.redis.RedisCacheService;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 实现UserDetailsService接口，实现loadUserByUsername方法去校验用户名
 * loadUserByUsername()会被过滤自动调用
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysUserService extends BaseService<SysUser> implements ISysUserService, UserDetailsService {
    private final SysUserMapper sysUserMapper;
    private final RedisCacheService redisCacheService;
    private final SecurityProperties securityProperties;

    @Override
    public void saveData(SysUser sysUser) {
        //用户名唯一
        SysUser user = new SysUser();
        user.setUserName(sysUser.getUserName());
        if (sysUserMapper.selectCountByBean(user) > 0){
            throw new BizException("用户名已存在");
        }

        sysUser.setUserUuid(UUIDUtil.newUUID());
        sysUserMapper.insertSelective(sysUser);
    }

    @Override
    public void updateData(SysUser sysUser) {
        //用户名唯一
        SysUser user = new SysUser();
        user.setUserName(sysUser.getUserName());
        List<SysUser> list = sysUserMapper.selectListByBean(user);
        for (SysUser item : list) {
            if (!item.getUserUuid().equals(sysUser.getUserUuid())){
                throw new BizException("用户名已存在");
            }
        }

        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public SysUser login(SysUser sysUser) {
        // ========== 1. 先检查是否是超级管理员 ==========
        SecurityProperties.UserProperties superAdmin = securityProperties.getUser();
        if (superAdmin.getName() != null
                && superAdmin.getName().equals(sysUser.getUserName())
                && superAdmin.getPassword() != null
                && superAdmin.getPassword().equals(sysUser.getUserPassword())) {

            log.info("超级管理员登录：{}", superAdmin.getName());

            // 构造虚拟 SysUser（超级管理员不在数据库表中）
            SysUser user = new SysUser();
            user.setUserUuid("sadmin-uuid");
            user.setUserName(superAdmin.getName());
            user.setUserPassword(superAdmin.getPassword());

            String token = this.getToken(user.getUserUuid(), user.getUserPassword());
            user.setToken(token);
            redisCacheService.setCache(user.getUserUuid(), user, 60 * 30, TimeUnit.SECONDS);
            return user;
        }

        // ========== 2. 普通用户：查数据库验证 ==========
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
        String token = this.getToken(user.getUserUuid(), user.getUserPassword());
        user.setToken(token);
        redisCacheService.setCache(user.getUserUuid(), user, 60 * 30, TimeUnit.SECONDS);
        return user;
    }

    private String getToken(String userId, String userPassword) {
        Date dateTime = DateUtils.getSecondsLastTime(60 * 30);

        // 生成 Token 的代码示例
        String token = JWT.create()
                .withAudience(userId)  // 添加这一行，将 userId 存入 audience
                .withExpiresAt(dateTime)
                .sign(Algorithm.HMAC256(userPassword));

        log.info("token已生成：" +token);
        log.info(DateUtils.date2StringTime(dateTime, DateUtils.DATE_FORMAT_SECOND) +"之后过期");
        return  token;
    }

    @Override
    public List<SysUser> queryPageList(SysUser sysUser) {
        return sysUserMapper.selectListByBean((sysUser));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        List<SysUser> list = sysUserMapper.selectListByBean(sysUser);
        if (list.isEmpty()){
            throw new UsernameNotFoundException(username);
        }

        SysUser found = list.getFirst();
        return new User(found.getUserName(), found.getUserPassword(), new ArrayList<>());
    }
}