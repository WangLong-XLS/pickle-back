package com.pickle.sys.service;

public interface IRAGAnswerService {
    String askQuestion(String question);

    String askWithFilter(String question);
}
