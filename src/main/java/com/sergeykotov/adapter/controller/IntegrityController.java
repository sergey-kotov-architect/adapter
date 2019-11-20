package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.domain.IntegrityDto;
import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.service.AuthorizationService;
import com.sergeykotov.adapter.service.IntegrityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/integrity")
public class IntegrityController {
    private final TaskQueue taskQueue;
    private final AuthorizationService authorizationService;
    private final IntegrityService integrityService;

    @Autowired
    public IntegrityController(TaskQueue taskQueue,
                               AuthorizationService authorizationService,
                               IntegrityService integrityService) {
        this.taskQueue = taskQueue;
        this.authorizationService = authorizationService;
        this.integrityService = integrityService;
    }

    @GetMapping
    public IntegrityDto verifyIntegrity(@RequestParam String authorization) {
        authorizationService.authorize(authorization);
        return integrityService.verify();
    }

    @PostMapping
    public void restoreIntegrity() {
        taskQueue.submitRestoreIntegrityTask(integrityService);
    }
}