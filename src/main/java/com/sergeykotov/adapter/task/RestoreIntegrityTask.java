package com.sergeykotov.adapter.task;

import com.sergeykotov.adapter.service.RuleService;

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