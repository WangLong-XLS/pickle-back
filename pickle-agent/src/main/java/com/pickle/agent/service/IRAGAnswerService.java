package com.pickle.agent.service;

public interface IRAGAnswerService {
    String askQuestion(String question);

    String askWithFilter(String question);
}
