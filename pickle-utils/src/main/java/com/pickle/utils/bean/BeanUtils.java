package com.pickle.utils.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.*;

public class BeanUtils {
    private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);
    static Map<String, BeanCopier> beanCopierMap = new HashMap<>();

    public static <T> List<T> copyPropertiesList(List<?> src, Class<T> clazz) {
        List<T> objs = new ArrayList<>();
        if (Objects.isNull(src)) {
            return objs;
        } else {
            for (Object t : src) {
                objs.add(copyPropertiesByCglib(t, clazz));
            }

            return objs;
        }
    }

    public static <T> T copyPropertiesByCglib(Object src, Class<T> clazz) {
        if (src == null) {
            return null;
        } else {
            T desc = null;

            try {
                desc = clazz.newInstance();
                String beanKey = src.getClass().toString() + clazz.toString();
                BeanCopier beanCopier = null;
                if (!beanCopierMap.containsKey(beanKey)) {
                    beanCopier = BeanCopier.create(src.getClass(), clazz, false);
                    beanCopierMap.put(beanKey, beanCopier);
                } else {
                    beanCopier = (BeanCopier)beanCopierMap.get(beanKey);
                }

                beanCopier.copy(src, desc, (Converter)null);
            } catch (InstantiationException | IllegalAccessException var5) {
                logger.error("", var5);
            }

            return desc;
        }
    }
}
