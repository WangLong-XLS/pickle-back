package com.pickle.sys.invoke;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

//@Component
public class SpringAiDbInvoke implements CommandLineRunner {
    @Autowired
    private ChatModel chatModel;


    @Override
    public void run(String... args) throws Exception {
   /*     String call = chatModel.call("你好我是小零食");
        System.out.println(call);*/

        // 基础用法(ChatModel)
//        ChatResponse response = chatModel.call(new Prompt("你好"));

        // 高级用法(ChatClient)
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultSystem("你是恋爱顾问")
                .build();

        String response = chatClient.prompt().user("你好我是小零食").call().content();
        System.out.println(response);
    }
}
