package com.sergeykotov.adapter.system;

import com.sergeykotov.adapter.domain.RuleSet;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class System2 implements System {
    private static final Logger log = Logger.getLogger(System2.class);

    private List<RuleSet> ruleSets = new ArrayList<>();

    @Override
    public String getName() {
        return "System2";
    }

    @Override
    public List<RuleSet> getRuleSets() {
        log.info("extracting Rule Sets...");
        log.info(ruleSets.size() + " Rule Sets have been extracted");
        return ruleSets;
    }

    @Override
    public boolean createRuleSet(RuleSet ruleSet) {
        log.info("creating Rule Set " + ruleSet + "...");
        if (ruleSets.contains(ruleSet)) {
            log.error("failed to create Rule Set " + ruleSet);
            return false;
        }
        ruleSets.add(ruleSet);
        log.info("Rule Set " + ruleSet + " has been created");
        return true;
    }

    @Override
    public boolean deleteRuleSet(RuleSet ruleSet) {
        log.info("deleting Rule Set " + ruleSet + "...");
        boolean deleted = ruleSets.remove(ruleSet);
        if (!deleted) {
            log.error("failed to delete Rule Set " + ruleSet);
            return false;
        }
        log.info("Rule Set " + ruleSet + " has been deleted");
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}