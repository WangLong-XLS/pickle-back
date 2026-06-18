package com.pickle.sys.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pickle.sys.bean.SysUser;
import com.pickle.sys.bean.SysUserOrg;
import com.pickle.sys.bean.SysUserRole;
import com.pickle.sys.config.SecurityProperties;
import com.pickle.sys.mapper.SysUserMapper;
import com.pickle.sys.mapper.SysUserOrgMapper;
import com.pickle.sys.mapper.SysUserRoleMapper;
import com.pickle.sys.service.ISysUserService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.redis.RedisCacheService;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 实现UserDetailsService接口，实现loadUserByUsername方法去校验用户名
 * loadUserByUsername()会被过滤自动调用
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysUserService extends BaseService<SysUser> implements ISysUserService {
    private final SysUserMapper sysUserMapper;
    private final RedisCacheService redisCacheService;
    private final SecurityProperties securityProperties;
    private final PasswordEncoder passwordEncoder;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysUserOrgMapper sysUserOrgMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveData(SysUser sysUser) {
        //用户名唯一
        SysUser user = new SysUser();
        user.setUserName(sysUser.getUserName());
        if (sysUserMapper.selectCountByBean(user) > 0){
            throw new BizException("用户名已存在");
        }

        sysUser.setUserUuid(UUIDUtil.newUUID());
        // ← 密码加密后再入库
        sysUser.setUserPassword(passwordEncoder.encode(sysUser.getUserPassword()));
        sysUserMapper.insertSelective(sysUser);

        //去添加角色信息
        List<SysUserRole> roles = new ArrayList<>();
        sysUser.getRoleUuidIn().forEach(e ->{
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserRoleUuid(UUIDUtil.newUUID());
            sysUserRole.setUserUuid(sysUser.getUserUuid());
            sysUserRole.setRoleUuid(e);

            roles.add(sysUserRole);
        });
        sysUserRoleMapper.batchInsert(roles);

        //去添加机构信息
        List<SysUserOrg> orgs = new ArrayList<>();
        sysUser.getOrgUuidIn().forEach(e ->{
            SysUserOrg sysUserOrg = new SysUserOrg();
            sysUserOrg.setUserOrgUuid(UUIDUtil.newUUID());
            sysUserOrg.setUserUuid(sysUser.getUserUuid());
            sysUserOrg.setOrgUuid(e);

            orgs.add(sysUserOrg);
        });
        sysUserOrgMapper.batchInsert(orgs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

        if (sysUser.getUserPassword() != null && !sysUser.getUserPassword().isEmpty()) {
            sysUser.setUserPassword(passwordEncoder.encode(sysUser.getUserPassword()));
        }
        sysUserMapper.updateByPrimaryKeySelective(sysUser);

        //去添加角色信息
        SysUserRole userRole = new SysUserRole();
        userRole.setUserUuid(sysUser.getUserUuid());
        sysUserRoleMapper.deleteByBean(userRole);

        List<SysUserRole> roles = new ArrayList<>();
        sysUser.getRoleUuidIn().forEach(e ->{
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserRoleUuid(UUIDUtil.newUUID());
            sysUserRole.setUserUuid(sysUser.getUserUuid());
            sysUserRole.setRoleUuid(e);

            roles.add(sysUserRole);
        });
        sysUserRoleMapper.batchInsert(roles);

        //去添加机构信息
        SysUserOrg userOrg = new SysUserOrg();
        userOrg.setUserUuid(sysUser.getUserUuid());
        sysUserOrgMapper.deleteByBean(userOrg);

        List<SysUserOrg> orgs = new ArrayList<>();
        sysUser.getOrgUuidIn().forEach(e ->{
            SysUserOrg sysUserOrg = new SysUserOrg();
            sysUserOrg.setUserOrgUuid(UUIDUtil.newUUID());
            sysUserOrg.setUserUuid(sysUser.getUserUuid());
            sysUserOrg.setOrgUuid(e);

            orgs.add(sysUserOrg);
        });
        sysUserOrgMapper.batchInsert(orgs);
    }

    @Override
    public SysUser login(SysUser sysUser) {
        // ========== 1. 先检查是否是超级管理员 ==========
        SecurityProperties.UserProperties superAdmin = securityProperties.getUser();
        if (superAdmin.getName() != null
                && superAdmin.getName().equals(sysUser.getUserName())) {

            // ← 用 matches 比较，配置文件的密码也要存 BCrypt hash
            if (passwordEncoder.matches(sysUser.getUserPassword(), superAdmin.getPassword())) {
                log.info("超级管理员登录：{}", superAdmin.getName());
                SysUser user = new SysUser();
                user.setUserUuid("sadmin-uuid");
                user.setUserName(superAdmin.getName());
                user.setOrgCode("sadmin_org_code");

                String token = this.getToken(user.getUserUuid(), superAdmin.getPassword());
                user.setToken(token);
                redisCacheService.setCache(user.getUserUuid(), user, 60 * 30, TimeUnit.SECONDS);
                return user;
            } else {
                throw new BizException("密码错误");
            }
        }

        // ========== 2. 普通用户：查数据库验证 ==========
        SysUser userBean = new SysUser();
        userBean.setUserName(sysUser.getUserName());
        List<SysUser> userList = sysUserMapper.selectListByBean(userBean);
        // 判断用户是否存在
        if (userList.isEmpty()){
            throw new BizException("用户名不存在");
        }

        SysUser user = userList.getFirst();
        // 用户存在，验证密码
        if (!passwordEncoder.matches(sysUser.getUserPassword(), user.getUserPassword())) {
            throw new BizException("密码错误");
        }

        //将角色和机构一并返回
        SysUserRole userRole = new SysUserRole();
        userRole.setUserUuid(sysUser.getUserUuid());
        List<String> roleUuidIn = sysUserRoleMapper.selectListByBean(userRole)
                .stream()
                .map(SysUserRole::getRoleUuid)
                .toList();

        SysUserOrg userOrg = new SysUserOrg();
        userOrg.setUserUuid(sysUser.getUserUuid());
        List<String> orgUuidIn = sysUserOrgMapper.selectListByBean(userOrg)
                .stream()
                .map(SysUserOrg::getOrgUuid)
                .toList();

        user.setRoleUuidIn(roleUuidIn);
        user.setOrgUuidIn(orgUuidIn);

        log.info("token开始生成");
        // 密码正确，返回成功，生成一个token
        String token = this.getToken(user.getUserUuid(), user.getUserPassword());
        user.setToken(token);
        redisCacheService.setCache(user.getUserUuid(), user, 60 * 30, TimeUnit.SECONDS);
        return user;
    }

    private String getToken(String userId, String userPassword) {
        // 去掉 withExpiresAt，JWT 永不过期，过期全靠 Redis TTL 控制
        String token = JWT.create()
                .withAudience(userId)
                .sign(Algorithm.HMAC256(userPassword));

        log.info("token已生成：{}", token);
        return token;
    }

    @Override
    public List<SysUser> queryPageList(SysUser sysUser) {
        return sysUserMapper.selectListByBean((sysUser));
    }
}