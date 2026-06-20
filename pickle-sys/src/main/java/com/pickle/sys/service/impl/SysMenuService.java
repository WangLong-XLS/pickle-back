package com.pickle.sys.service.impl;

import com.pickle.sys.bean.SysMenu;
import com.pickle.sys.bean.SysRoleMenu;
import com.pickle.sys.mapper.SysMenuMapper;
import com.pickle.sys.mapper.SysRoleMenuMapper;
import com.pickle.sys.service.ISysMenuService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.enums.YesOrNo;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuService extends BaseService<SysMenu> implements ISysMenuService {
    private final SysMenuMapper sysMenuMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> getMenusByRole(SysMenu sysMenu) {
        List<SysMenu> list = sysMenuMapper.getMenusByRole(sysMenu);

        // 1. 找出所有根节点（parentUuid 为 null）
        List<SysMenu> roots = list.stream()
                .filter(m -> StringUtils.isBlank(m.getParentUuid()))
                .sorted(Comparator.comparing(this::getOrderValue))
                .toList();

        // 2. 为每个根节点递归挂载子节点
        for (SysMenu root : roots) {
            root.setSysMenuIn(buildChildren(root, list));
        }

        return roots;
    }

    @Override
    public List<SysMenu> queryPageList(SysMenu sysMenu) {
        List<SysMenu> list = sysMenuMapper.queryPageList(sysMenu);
        list.forEach(e ->{
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuUuid(e.getMenuUuid());
            List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectListByBean(sysRoleMenu);

            e.setMenuTypeName(e.getMenuType());
            e.setVisibleName(e.getVisible().equals(YesOrNo.YES.getCode()) ? YesOrNo.YES.getMessage() : YesOrNo.NO.getMessage());
            e.setRoleUuidIn(roleMenus.stream().map(SysRoleMenu::getRoleUuid).toList());
        });
        return list;
    }

    @Override
    public List<SysMenu> selectParentList(SysMenu sysMenu) {
        List<SysMenu> list = sysMenuMapper.selectParentList(sysMenu);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveData(SysMenu sysMenu) {
        SysMenu menu = new SysMenu();
        menu.setMenuType(sysMenu.getMenuType());
        menu.setMenuName(sysMenu.getMenuName());
        if (sysMenuMapper.selectCountByBean(menu) > 0){
            throw new BizException("改类型菜单已经存在！");
        }

        sysMenu.setMenuUuid(UUIDUtil.newUUID());
        sysMenuMapper.insertSelective(sysMenu);

        //去添加角色
        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        sysMenu.getRoleUuidIn().forEach(e -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleMenuUuid(UUIDUtil.newUUID());
            sysRoleMenu.setMenuUuid(sysMenu.getMenuUuid());
            sysRoleMenu.setRoleUuid(e);
            sysRoleMenus.add(sysRoleMenu);
        });
        sysRoleMenuMapper.batchInsert(sysRoleMenus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateData(SysMenu sysMenu) {
        SysMenu menu = new SysMenu();
        menu.setMenuType(sysMenu.getMenuType());
        menu.setMenuName(sysMenu.getMenuName());
        List<SysMenu> list = sysMenuMapper.selectListByBean(menu);
        list.forEach(e ->{
            if (!(e.getMenuUuid().equals(sysMenu.getMenuUuid()))){
                throw new BizException("改类型菜单已经存在！");
            }
        });

        sysMenuMapper.updateByPrimaryKeySelective(sysMenu);

        //去添加角色
        SysRoleMenu roleMenu = new SysRoleMenu();
        roleMenu.setMenuUuid(sysMenu.getMenuUuid());
        sysRoleMenuMapper.deleteByBean(roleMenu);

        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        sysMenu.getRoleUuidIn().forEach(e -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleMenuUuid(UUIDUtil.newUUID());
            sysRoleMenu.setMenuUuid(sysMenu.getMenuUuid());
            sysRoleMenu.setRoleUuid(e);
            sysRoleMenus.add(sysRoleMenu);
        });
        sysRoleMenuMapper.batchInsert(sysRoleMenus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteData(SysMenu sysMenu) {
        //先去删除角色权限表
        SysRoleMenu roleMenu = new SysRoleMenu();
        roleMenu.setMenuUuidIn(sysMenu.getMenuUuidIn());
        sysRoleMenuMapper.deleteByBean(roleMenu);

        if (!sysMenu.getMenuUuidIn().isEmpty()){
            sysMenuMapper.batchDeleteByPrimaryKey(sysMenu.getMenuUuidIn());
        }
    }

    /**
     * 递归构建子节点
     */
    private List<SysMenu> buildChildren(SysMenu parent, List<SysMenu> allMenus) {
        // 找出所有 parentUuid 等于当前节点 menuUuid 的子节点
        List<SysMenu> children = allMenus.stream()
                .filter(m -> parent.getMenuUuid().equals(m.getParentUuid()))
                .sorted(Comparator.comparing(this::getOrderValue))
                .collect(Collectors.toList());

        // 递归：为每个子节点继续挂载它的子节点
        for (SysMenu child : children) {
            child.setSysMenuIn(buildChildren(child, allMenus));
        }

        return children;
    }

    /**
     * 安全获取排序值
     */
    private int getOrderValue(SysMenu m) {
        if (m.getMenuOrder() == null) return 0;
        try {
            return Integer.parseInt(m.getMenuOrder());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}