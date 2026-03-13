package com.pickle.sys.validator;

import com.pickle.sys.bean.entity.SsJbxxEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Slf4j
public class DataValidator {
    
    // 手机号正则
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    // 邮箱正则
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    // 身份证号正则（简单版）
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$|^[1-9]\\d{5}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}$");
    

    /**
     * 校验并清理数据（去除前后空格等）
     */
    public static String cleanAndValidate(SsJbxxEntity entity, int rowNum) {
        // 执行校验
        StringBuffer buffer = new StringBuffer();

        // 去除字符串字段的前后空格
        if (!StringUtils.hasText(entity.getZbfMc())) {
            buffer.append(String.format("第%d行：主办方名称不能为空", rowNum));
        }
        if (!StringUtils.hasText(entity.getSsMc())) {
            buffer.append(String.format("第%d行：赛事名称不能为空\n", rowNum));
        }

        return buffer.toString();
    }
}