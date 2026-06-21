package com.pickle.utils.list;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用树形结构构建工具（支持按指定字段排序）
 */
public class TreeUtils {

    /**
     * 构建树形结构（不排序）
     */
    public static <T> List<T> buildTree(
            List<T> list,
            Function<T, String> getId,
            Function<T, String> getParentId,
            BiConsumer<T, List<T>> setChildren) {
        return buildTree(list, getId, getParentId, setChildren, null);
    }

    /**
     * 构建树形结构（按指定字段排序，字段类型必须实现 Comparable）
     * @param list 所有节点列表
     * @param getId 获取节点ID的函数
     * @param getParentId 获取父节点ID的函数
     * @param setChildren 设置子节点列表的函数
     * @param sortField 排序字段提取函数（传入的字段类型必须实现 Comparable，如 String、Integer、Date 等）
     * @param <T> 节点类型
     * @param <R> 排序字段类型（必须实现 Comparable）
     * @return 树形结构根节点列表
     */
    public static <T, R extends Comparable<R>> List<T> buildTree(
            List<T> list,
            Function<T, String> getId,
            Function<T, String> getParentId,
            BiConsumer<T, List<T>> setChildren,
            Function<T, R> sortField) {

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 构建比较器（处理 null 值，null 排最后）
        Comparator<T> comparator = null;
        if (sortField != null) {
            comparator = Comparator.comparing(
                    sortField,
                    Comparator.nullsLast(Comparable::compareTo)
            );
        }

        // 按父ID分组
        Map<String, List<T>> parentMap = list.stream()
                .filter(item -> getParentId.apply(item) != null)
                .collect(Collectors.groupingBy(getParentId));

        // 找出根节点
        List<T> roots = list.stream()
                .filter(item -> StringUtils.isBlank(getParentId.apply(item)))
                .collect(Collectors.toList());

        // 递归挂载子节点
        for (T root : roots) {
            setChildren.accept(root, buildChildren(root, parentMap, getId, setChildren, comparator));
        }

        // 对根节点排序
        if (comparator != null) {
            roots.sort(comparator);
        }

        return roots;
    }

    private static <T> List<T> buildChildren(
            T parent,
            Map<String, List<T>> parentMap,
            Function<T, String> getId,
            BiConsumer<T, List<T>> setChildren,
            Comparator<T> comparator) {

        String parentId = getId.apply(parent);
        List<T> children = parentMap.getOrDefault(parentId, new ArrayList<>());

        // 递归构建子节点的子节点
        for (T child : children) {
            setChildren.accept(child, buildChildren(child, parentMap, getId, setChildren, comparator));
        }

        // 对子节点排序
        if (comparator != null) {
            children.sort(comparator);
        }

        return children;
    }

    @FunctionalInterface
    public interface BiConsumer<T, U> {
        void accept(T t, U u);
    }
}