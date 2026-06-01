package com.pickle.utils.date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Date;

@JsonComponent
public class DateDeserializer extends JsonDeserializer<Date> {
    
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getText();
        try {
            // 只解析日期，时间是 00:00:00
            return DateUtils.string2Date(dateStr, DateUtils.DATE_FORMAT_DAY);
        } catch (Exception e) {
            throw new RuntimeException("日期格式错误，应为 yyyy-MM-dd");
        }
    }
}