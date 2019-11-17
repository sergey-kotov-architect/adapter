package com.sergeykotov.adapter.task;

import com.sergeykotov.adapter.domain.RuleSet;
import com.sergeykotov.adapter.service.RuleSetService;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteRuleSetTask implements Task {
    @Autowired
    private RuleSetService ruleSetService;
    private final RuleSet ruleSet;

    public DeleteRuleSetTask(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
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