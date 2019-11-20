package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.queue.TaskQueueDto;
import com.sergeykotov.adapter.service.AuthorizationService;
import com.sergeykotov.adapter.task.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/queue")
public class QueueController {
    private final TaskQueue taskQueue;
    private final AuthorizationService authorizationService;

    @Autowired
    public QueueController(TaskQueue taskQueue, AuthorizationService authorizationService) {
        this.taskQueue = taskQueue;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public TaskQueueDto getTaskQueueDto(@RequestParam String authorization) {
        authorizationService.authorize(authorization);
        return taskQueue.getTaskQueueDto();
    }

    @GetMapping("/task")
    public List<TaskResult> getTaskResults(@RequestParam String authorization) {
        authorizationService.authorize(authorization);
        return taskQueue.getTaskResults();
    }
}