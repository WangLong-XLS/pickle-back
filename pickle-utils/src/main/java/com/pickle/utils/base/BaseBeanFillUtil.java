package com.pickle.utils.base;

import com.github.pagehelper.util.StringUtil;
import java.util.Date;

public class BaseBeanFillUtil {

    public static void fillForInsert(BaseBean bean, String userId, String orgCode) {
        Date now = new Date();
        if (StringUtil.isEmpty(bean.getCjRyDm())) bean.setCjRyDm(userId);
        if (bean.getCjSj() == null) bean.setCjSj(now);
        bean.setXgRyDm(userId);
        bean.setXgSj(now);
        if (StringUtil.isEmpty(bean.getSjgsJgDm())) bean.setSjgsJgDm(orgCode);
    }

    public static void fillForUpdate(BaseBean bean, String userId) {
        bean.setXgRyDm(userId);
        bean.setXgSj(new Date());
    }
}
