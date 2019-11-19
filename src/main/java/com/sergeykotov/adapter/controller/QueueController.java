package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.queue.TaskQueueDto;
import com.sergeykotov.adapter.task.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/queue")
public class QueueController {
    private final TaskQueue taskQueue;

    @Autowired
    public QueueController(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @GetMapping
    public TaskQueueDto getTaskQueueDto() {
        return taskQueue.getTaskQueueDto();
    }

    @GetMapping("/task")
    public List<TaskResult> getTaskResults() {
        return taskQueue.getTaskResults();
    }
}