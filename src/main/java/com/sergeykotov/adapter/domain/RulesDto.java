package com.sergeykotov.adapter.domain;

import java.util.List;
import java.util.Map;

public class RulesDto {
    private Map<Rule, List<String>> ruleSetSystemsMap;

    public RulesDto() {
    }

    public Map<Rule, List<String>> getRuleSetSystemsMap() {
        return ruleSetSystemsMap;
    }

    public void setRuleSetSystemsMap(Map<Rule, List<String>> ruleSetSystemsMap) {
        this.ruleSetSystemsMap = ruleSetSystemsMap;
    }
}