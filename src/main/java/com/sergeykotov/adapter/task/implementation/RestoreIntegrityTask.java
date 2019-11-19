package com.sergeykotov.adapter.task.implementation;

import com.sergeykotov.adapter.service.IntegrityService;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskResult;
import com.sergeykotov.adapter.task.TaskType;

public class RestoreIntegrityTask implements Task {
    private final IntegrityService integrityService;

    public RestoreIntegrityTask(IntegrityService integrityService) {
        this.integrityService = integrityService;
    }

    @Override
    public TaskType getType() {
        return TaskType.RESTORE_INTEGRITY;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public TaskResult execute() {
        return integrityService.restore();
    }

    @Override
    public String toString() {
        return getType().toString();
    }
}