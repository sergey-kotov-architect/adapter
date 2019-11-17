package com.sergeykotov.adapter.task;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.service.RuleService;

public class DeleteRuleTask implements Task {
    private final RuleService ruleService;
    private final Rule rule;

    public DeleteRuleTask(RuleService ruleService, Rule rule) {
        this.ruleService = ruleService;
        this.rule = rule;
    }

    @Override
    public TaskType getType() {
        return TaskType.DELETE_RULE;
    }

    @Override
    public String getName() {
        return rule.getName();
    }

    @Override
    public void execute() {
        ruleService.delete(rule);
    }

    @Override
    public String toString() {
        return getType() + " " + getName();
    }
}