package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.domain.IntegrityDto;
import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.service.IntegrityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/integrity")
public class IntegrityController {
    private final TaskQueue taskQueue;
    private final IntegrityService integrityService;

    @Autowired
    public IntegrityController(TaskQueue taskQueue, IntegrityService integrityService) {
        this.taskQueue = taskQueue;
        this.integrityService = integrityService;
    }

    @GetMapping
    public IntegrityDto verifyIntegrity() {
        return integrityService.verify();
    }

    @PostMapping
    public void restoreIntegrity() {
        taskQueue.submitRestoreIntegrityTask(integrityService);
    }
}