package com.sergeykotov.adapter.system;

import com.sergeykotov.adapter.domain.RuleSet;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class System1 implements System {
    private static final Logger log = Logger.getLogger(System1.class);

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