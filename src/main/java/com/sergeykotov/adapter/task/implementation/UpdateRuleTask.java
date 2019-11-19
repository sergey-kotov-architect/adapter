package com.sergeykotov.adapter.task.implementation;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.service.RuleService;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskResult;
import com.sergeykotov.adapter.task.TaskType;

public class UpdateRuleTask implements Task {
    private final RuleService ruleService;
    private final Rule rule;

    public UpdateRuleTask(RuleService ruleService, Rule rule) {
        this.ruleService = ruleService;
        this.rule = rule;
    }

    @Override
    public TaskType getType() {
        return TaskType.UPDATE_RULE;
    }

    @Override
    public String getName() {
        return rule.getName();
    }

    @Override
    public TaskResult execute() {
        return ruleService.update(rule);
    }

    @Override
    public String toString() {
        return getType() + " " + getName();
    }
}