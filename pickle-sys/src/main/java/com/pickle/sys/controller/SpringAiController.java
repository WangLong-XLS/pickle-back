package com.pickle.sys.controller;

import com.pickle.sys.advisor.MyLoggerAdvisor;
import com.pickle.sys.advisor.ReReadingAdvisor;
import com.pickle.sys.bean.GgFj;
import com.pickle.utils.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class SpringAiController extends BaseController<GgFj> {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ChatMemory chatMemory;

    @Autowired
    private ChatModel chatModel;


    @GetMapping("/testDialogue")
    String generation(String userInput) {

        var chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new MyLoggerAdvisor(),
                        new ReReadingAdvisor(),
                        QuestionAnswerAdvisor.builder(vectorStore).build()
                )
                .build();

        var conversationId = "678";
        String response = chatClient.prompt()
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(userInput)
                .call()
                .content();

        return response;
    }


    /**
     * 和 RAG 知识库进行对话
     *
     * @param message
     * @return
     */
/*    @RequestMapping("/testRag")
    public String doChatWithRag(@RequestParam("message") String message) {
        String chatId = UUIDUtil.newUUID();
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                        .param("topK", 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用知识库问答
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }*/


}