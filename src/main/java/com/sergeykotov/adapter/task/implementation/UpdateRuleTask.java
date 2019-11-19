package com.sergeykotov.adapter.task.implementation;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.service.RuleService;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskResult;
import com.sergeykotov.adapter.task.TaskType;

import java.time.LocalDateTime;

public class UpdateRuleTask implements Task {
    private final RuleService ruleService;
    private final Rule rule;
    private final LocalDateTime submissionTime = LocalDateTime.now();

    public UpdateRuleTask(RuleService ruleService, Rule rule) {
        this.ruleService = ruleService;
        this.rule = rule;
    }

    @Override
    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    @Override
    public TaskType getName() {
        return TaskType.UPDATE_RULE;
    }

    @Override
    public String getNote() {
        return rule.getName();
    }

    @Override
    public TaskResult execute() {
        return ruleService.update(rule);
    }

    @Override
    public String toString() {
        return getName() + " " + getNote();
    }
}