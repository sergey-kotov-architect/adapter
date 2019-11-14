package com.sergeykotov.adapter.system;

import com.sergeykotov.adapter.domain.RuleSet;

import java.util.List;

public interface System {
    String getName();

    List<RuleSet> getRuleSets();

    boolean createRuleSet(RuleSet ruleSet);

    boolean deleteRuleSet(RuleSet ruleSet);
}