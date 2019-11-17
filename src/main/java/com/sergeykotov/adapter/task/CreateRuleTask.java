package com.sergeykotov.adapter.task;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.service.RuleService;

public class CreateRuleTask implements Task {
    private final RuleService ruleService;
    private final Rule rule;

    public CreateRuleTask(RuleService ruleService, Rule rule) {
        this.ruleService = ruleService;
        this.rule = rule;
    }

    @Override
    public TaskType getType() {
        return TaskType.CREATE_RULE;
    }

    @Override
    public String getName() {
        return rule.getName();
    }

    @Override
    public void execute() {
        ruleService.create(rule);
    }

    @Override
    public String toString() {
        return getType() + " " + getName();
    }
}