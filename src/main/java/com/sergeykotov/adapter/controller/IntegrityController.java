package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.dto.IntegrityDto;
import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.service.AuthorizationService;
import com.sergeykotov.adapter.service.IntegrityService;
import com.sergeykotov.adapter.task.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public IntegrityDto verifyIntegrity(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return integrityService.verify();
    }

    @PostMapping
    public TaskDto restoreIntegrity(@RequestHeader String authorization, @RequestBody @Valid List<Rule> rules) {
        authorizationService.authorize(authorization);
        return taskQueue.submitRestoreIntegrityTask(integrityService, rules);
    }
}