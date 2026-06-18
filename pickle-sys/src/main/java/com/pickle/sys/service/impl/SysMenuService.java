package com.pickle.sys.service.impl;

import com.pickle.sys.bean.SysMenu;
import com.pickle.sys.mapper.SysMenuMapper;
import com.pickle.sys.service.ISysMenuService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.enums.YesOrNo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuService extends BaseService<SysMenu> implements ISysMenuService {
    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> getMenusByRole(SysMenu sysMenu) {
        List<SysMenu> list = sysMenuMapper.getMenusByRole(sysMenu);

        // 1. 找出所有根节点（parentUuid 为 null）
        List<SysMenu> roots = list.stream()
                .filter(m -> m.getParentUuid() == null)
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
            e.setMenuTypeName(e.getMenuType());
            e.setVisibleName(e.getVisible().equals(YesOrNo.YES.getCode()) ? YesOrNo.YES.getMessage() : YesOrNo.NO.getMessage());
        });
        return list;
    }

    @Override
    public List<SysMenu> selectParentList(SysMenu sysMenu) {
        List<SysMenu> list = sysMenuMapper.selectParentList(sysMenu);
        return list;
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