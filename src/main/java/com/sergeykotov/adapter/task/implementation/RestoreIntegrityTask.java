package com.sergeykotov.adapter.task.implementation;

import com.sergeykotov.adapter.service.RuleService;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskType;

public class RestoreIntegrityTask implements Task {
    private final RuleService ruleService;

    public RestoreIntegrityTask(RuleService ruleService) {
        this.ruleService = ruleService;
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
    public void execute() {
        ruleService.restoreIntegrity();
    }

    @Override
    public String toString() {
        return getType().toString();
    }
}