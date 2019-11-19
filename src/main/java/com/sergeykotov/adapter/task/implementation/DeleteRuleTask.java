package com.sergeykotov.adapter.task.implementation;

import com.sergeykotov.adapter.service.RuleService;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskResult;
import com.sergeykotov.adapter.task.TaskType;

import java.time.LocalDateTime;

public class DeleteRuleTask implements Task {
    private final RuleService ruleService;
    private final long id;
    private final LocalDateTime submissionTime = LocalDateTime.now();

    public DeleteRuleTask(RuleService ruleService, long id) {
        this.ruleService = ruleService;
        this.id = id;
    }

    @Override
    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    @Override
    public TaskType getName() {
        return TaskType.DELETE_RULE;
    }

    @Override
    public String getNote() {
        return "rule ID " + id;
    }

    @Override
    public TaskResult execute() {
        return ruleService.delete(id);
    }

    @Override
    public String toString() {
        return getName() + " " + getNote();
    }
}