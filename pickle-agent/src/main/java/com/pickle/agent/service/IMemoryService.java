package com.pickle.agent.service;

public interface IMemoryService {
    void storeMemory(String userId, String userMessage, String assistantMessage);

    String retrieveMemories(String userId, String userMessage);
}
