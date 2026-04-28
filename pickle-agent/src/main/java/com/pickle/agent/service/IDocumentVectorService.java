package com.pickle.agent.service;

import org.springframework.web.multipart.MultipartFile;


public interface IDocumentVectorService {

    int vectorDocumentSave(MultipartFile file);
}