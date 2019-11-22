package com.sergeykotov.adapter.task.implementation;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.service.RuleService;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskResult;
import com.sergeykotov.adapter.task.TaskType;

import java.time.LocalDateTime;

public class DeleteRuleTask implements Task {
    private final long ID;
    private final RuleService ruleService;
    private final Rule rule;
    private final LocalDateTime submissionTime = LocalDateTime.now();

    public DeleteRuleTask(long ID, RuleService ruleService, Rule rule) {
        this.ID = ID;
        this.ruleService = ruleService;
        this.rule = rule;
    }

    @Override
    public long getID() {
        return ID;
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
        return rule.toString();
    }

    @Override
    public TaskResult execute() {
        return ruleService.delete(rule);
    }

    @Override
    public String toString() {
        return getName() + " " + getNote();
    }
}