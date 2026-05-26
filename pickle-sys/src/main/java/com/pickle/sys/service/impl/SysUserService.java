package com.pickle.sys.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pickle.sys.bean.SysUser;
import com.pickle.sys.mapper.SysUserMapper;
import com.pickle.sys.service.ISysUserService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.date.DateUtils;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.redis.RedisCacheService;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SysUserService extends BaseService<SysUser> implements ISysUserService {
    private final SysUserMapper sysUserMapper;
    private final RedisCacheService redisCacheService;

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
}