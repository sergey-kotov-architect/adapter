package com.sergeykotov.adapter.system;

import com.sergeykotov.adapter.domain.RuleSet;
import com.sergeykotov.adapter.exception.ExtractionException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class System1 implements System {
    private static final Logger log = Logger.getLogger(System1.class);
    private static final String NAME = "System1";

    private List<RuleSet> ruleSets = new ArrayList<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<RuleSet> getRuleSets() {
        log.info("extracting Rule Sets...");
        try {
            Thread.sleep(2_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("Rule Set extraction has been interrupted");
            throw new ExtractionException();
        }
        log.info(ruleSets.size() + " Rule Sets have been extracted");
        return ruleSets;
    }

    @Override
    public boolean createRuleSet(RuleSet ruleSet) {
        log.info("creating Rule Set " + ruleSet + "...");
        try {
            Thread.sleep(15_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("Rule Set creation has been interrupted");
            return false;
        }
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
        try {
            Thread.sleep(10_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("Rule Set deletion has been interrupted");
            return false;
        }
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