package com.sergeykotov.adapter.domain;

import java.util.List;
import java.util.Map;

public class RuleSetsDto {
    private Map<RuleSet, List<String>> ruleSetSystemsMap;

    public RuleSetsDto() {
    }

    public Map<RuleSet, List<String>> getRuleSetSystemsMap() {
        return ruleSetSystemsMap;
    }

    public void setRuleSetSystemsMap(Map<RuleSet, List<String>> ruleSetSystemsMap) {
        this.ruleSetSystemsMap = ruleSetSystemsMap;
    }
}