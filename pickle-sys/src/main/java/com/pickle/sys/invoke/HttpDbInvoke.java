package com.pickle.sys.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class HttpDbInvoke {
    public static void main(String[] args) {
        String apiKey = TestApiKey.API_KEY;
        // 修正：去掉 /coding，使用标准的数据面 API 路径
        String url = "https://ark.cn-beijing.volces.com/api/v3/responses";

        // 构建请求体
        JSONObject requestBody = JSONUtil.createObj()
                .set("model", "doubao-seed-2-0-code-preview-260215")
                .set("input", "你好，我是小零食，正在学习ai");

        try {
            cn.hutool.http.HttpResponse httpResponse = HttpRequest.post(url)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", ContentType.JSON.getValue())
                    .body(requestBody.toString())
                    .timeout(30000)
                    .execute();

            int statusCode = httpResponse.getStatus();
            String response = httpResponse.body();

            System.out.println("状态码: " + statusCode);
            System.out.println("原始响应: " + response);

            if (statusCode == 200 && response != null && !response.trim().isEmpty()) {
                JSONObject jsonResponse = JSONUtil.parseObj(response);
                System.out.println("\n格式化输出:");
                System.out.println(JSONUtil.toJsonPrettyStr(jsonResponse));
            } else if (statusCode == 404) {
                System.err.println("请检查：1. URL 路径是否正确 2. 模型名称是否有效 3. API Key 是否有权限");
            }
        } catch (Exception e) {
            System.err.println("请求异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}