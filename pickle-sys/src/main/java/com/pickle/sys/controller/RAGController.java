package com.pickle.sys.controller;

import com.pickle.sys.service.IRAGAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rag")
@RequiredArgsConstructor
public class RAGController {

    private final IRAGAnswerService ragAnswerService;

    @RequestMapping("/ask")
    public String ask(@RequestParam String question) {
        return ragAnswerService.askQuestion(question);
    }

    @RequestMapping("/ask/filter")
    public String askWithFilter(@RequestParam String question) {
        return ragAnswerService.askWithFilter(question);
    }
}