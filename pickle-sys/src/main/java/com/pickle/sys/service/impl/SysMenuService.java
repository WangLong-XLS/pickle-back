package com.pickle.sys.service.impl;

import com.pickle.sys.bean.SysMenu;
import com.pickle.sys.bean.SysRoleMenu;
import com.pickle.sys.mapper.SysMenuMapper;
import com.pickle.sys.mapper.SysRoleMenuMapper;
import com.pickle.sys.service.ISysMenuService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.enums.YesOrNo;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.list.TreeUtils;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuService extends BaseService<SysMenu> implements ISysMenuService {
    private final SysMenuMapper sysMenuMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;

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

    @Override
    public List<SysMenu> queryTreeList(SysMenu sysMenu) {
        List<SysMenu> list = sysMenuMapper.queryTreeList(sysMenu);
        List<SysMenu> buildTree = TreeUtils.buildTree(
                list,
                SysMenu::getMenuUuid,      // 获取节点ID
                SysMenu::getParentUuid,    // 获取父节点ID
                SysMenu::setSysMenuIn,     // 设置子节点列表
                SysMenu::getMenuOrder      // 排序字段
        );
        buildTree.forEach(e ->{
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuUuid(e.getMenuUuid());
            List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectListByBean(sysRoleMenu);
            e.setRoleUuidIn(roleMenus.stream().map(SysRoleMenu::getRoleUuid).toList());
        });
        return buildTree;
    }

}