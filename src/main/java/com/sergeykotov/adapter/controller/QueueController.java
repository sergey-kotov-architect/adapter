package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.dto.TaskResultsDeletion;
import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.queue.TaskQueueDto;
import com.sergeykotov.adapter.service.AuthorizationService;
import com.sergeykotov.adapter.task.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public TaskQueueDto getTaskQueueDto(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return taskQueue.getTaskQueueDto();
    }

    @GetMapping("/task")
    public List<TaskResult> getTaskResults(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return taskQueue.getTaskResults();
    }

    @DeleteMapping("/task")
    public TaskResultsDeletion deleteTaskResults(@RequestHeader String authorization,
                                                 @RequestHeader("date-time") String dateTime) {
        authorizationService.authorize(authorization);
        return taskQueue.deleteTaskResults(dateTime);
    }
}