package com.sergeykotov.adapter.task;

import com.sergeykotov.adapter.domain.RuleSet;
import com.sergeykotov.adapter.service.RuleSetService;

public class CreateRuleSetTask implements Task {
    private final RuleSetService ruleSetService;
    private final RuleSet ruleSet;

    public CreateRuleSetTask(RuleSet ruleSet, RuleSetService ruleSetService) {
        this.ruleSet = ruleSet;
        this.ruleSetService = ruleSetService;
    }

    @Override
    public String getName() {
        return "create Rule Set " + ruleSet.getName();
    }

    @Override
    public void execute() {
        ruleSetService.create(ruleSet);
    }
}