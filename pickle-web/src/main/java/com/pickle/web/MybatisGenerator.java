package com.pickle.web;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MybatisGenerator {
    public static void main(String[] args) {
        MybatisGenerator generator = new MybatisGenerator();
        generator.run();
    }

    public void run() {
        try {
            // 加载 MyBatis Generator 配置文件
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("generatorConfig.xml");
            if (resourceAsStream == null) {
                System.err.println("generatorConfig.xml 配置文件未找到，请检查文件路径！");
                return;
            }

            // 解析配置文件
            List<String> warnings = new ArrayList<>();
            ConfigurationParser parser = new ConfigurationParser(warnings);
            Configuration config = parser.parseConfiguration(resourceAsStream);

            // 默认回调，覆盖现有文件
            DefaultShellCallback callback = new DefaultShellCallback(true);

            // 创建 MyBatis Generator 实例
            MyBatisGenerator generator = new MyBatisGenerator(config, callback, warnings);

            // 执行代码生成
            generator.generate(null, null);

            // 输出警告信息
            for (String warning : warnings) {
                System.err.println("> 注意: " + warning);
            }

        } catch (Exception e) {
            // 输出异常堆栈信息，便于调试
            e.printStackTrace();
        }
    }
}
