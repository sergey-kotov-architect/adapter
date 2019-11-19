package com.sergeykotov.adapter.task.implementation;

import com.sergeykotov.adapter.service.IntegrityService;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskResult;
import com.sergeykotov.adapter.task.TaskType;

import java.time.LocalDateTime;

public class RestoreIntegrityTask implements Task {
    private final IntegrityService integrityService;
    private final LocalDateTime submissionTime = LocalDateTime.now();

    public RestoreIntegrityTask(IntegrityService integrityService) {
        this.integrityService = integrityService;
    }

    @Override
    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    @Override
    public TaskType getName() {
        return TaskType.RESTORE_INTEGRITY;
    }

    @Override
    public String getNote() {
        return "";
    }

    @Override
    public TaskResult execute() {
        return integrityService.restore();
    }

    @Override
    public String toString() {
        return getName().toString();
    }
}