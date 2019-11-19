package com.sergeykotov.adapter.task.implementation;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.service.RuleService;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskResult;
import com.sergeykotov.adapter.task.TaskType;

import java.time.LocalDateTime;

public class CreateRuleTask implements Task {
    private final RuleService ruleService;
    private final Rule rule;
    private final LocalDateTime submissionTime = LocalDateTime.now();

    public CreateRuleTask(RuleService ruleService, Rule rule) {
        this.ruleService = ruleService;
        this.rule = rule;
    }

    @Override
    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    @Override
    public TaskType getName() {
        return TaskType.CREATE_RULE;
    }

    @Override
    public String getNote() {
        return rule.getName();
    }

    @Override
    public TaskResult execute() {
        return ruleService.create(rule);
    }

    @Override
    public String toString() {
        return getName() + " " + getNote();
    }
}