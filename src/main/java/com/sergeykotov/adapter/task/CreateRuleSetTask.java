package com.sergeykotov.adapter.task;

import com.sergeykotov.adapter.domain.RuleSet;
import com.sergeykotov.adapter.service.RuleSetService;

public class CreateRuleSetTask implements Task {
    private final RuleSetService ruleSetService;
    private final RuleSet ruleSet;

    public CreateRuleSetTask(RuleSetService ruleSetService, RuleSet ruleSet) {
        this.ruleSetService = ruleSetService;
        this.ruleSet = ruleSet;
    }

    @Override
    public TaskType getType() {
        return TaskType.CREATE_RULE_SET;
    }

    @Override
    public String getName() {
        return ruleSet.getName();
    }

    @Override
    public void execute() {
        ruleSetService.create(ruleSet);
    }

    @Override
    public String toString() {
        return getType() + " " + getName();
    }
}