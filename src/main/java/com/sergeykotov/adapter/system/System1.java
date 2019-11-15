package com.sergeykotov.adapter.system;

import com.sergeykotov.adapter.domain.RuleSet;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class System1 implements System {
    @Override
    public String getName() {
        return "System1";
    }

    @Override
    public List<RuleSet> getRuleSets() {
        return Collections.emptyList();
    }

    @Override
    public boolean createRuleSet(RuleSet ruleSet) {
        return false;
    }

    @Override
    public boolean deleteRuleSet(RuleSet ruleSet) {
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }
}