package com.pickle.agent.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tool")
public class ToolController {

    @RequestMapping("/test")
    public String test() {
        List<String> list = new ArrayList<>();
        list.add("test");
        System.out.println(list);
        return "test";
    }
}
