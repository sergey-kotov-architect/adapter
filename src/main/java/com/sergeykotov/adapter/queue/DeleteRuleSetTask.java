package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.domain.RuleSet;
import com.sergeykotov.adapter.service.RuleSetService;

public class DeleteRuleSetTask implements Task {
    private final RuleSetService ruleSetService;
    private final RuleSet ruleSet;

    public DeleteRuleSetTask(RuleSet ruleSet, RuleSetService ruleSetService) {
        this.ruleSet = ruleSet;
        this.ruleSetService = ruleSetService;
    }

    @Override
    public String getName() {
        return "delete Rule Set " + ruleSet.getName();
    }

    @Override
    public void execute() {
        ruleSetService.delete(ruleSet);
    }
}