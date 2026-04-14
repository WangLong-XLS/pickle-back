package com.pickle.sys.invoke;

import com.volcengine.ark.runtime.service.ArkService;
import com.volcengine.ark.runtime.model.responses.request.*;
import com.volcengine.ark.runtime.model.responses.response.ResponseObject;

public class SdkDbInvoke {
    public static void main(String[] args) {
        String apiKey = TestApiKey.API_KEY;
        ArkService arkService = ArkService.builder()
                .apiKey(apiKey)
                .baseUrl("https://ark.cn-beijing.volces.com/api/coding/v3")
                .build();

        CreateResponsesRequest request = CreateResponsesRequest.builder()
                .model("doubao-seed-2-0-code-preview-260215")
                .input(ResponsesInput.builder().stringValue("你好，我是小零食，正在学习ai").build())
                .build();

        ResponseObject resp = arkService.createResponse(request);
        System.out.println(resp);

        arkService.shutdownExecutor();
    }
}