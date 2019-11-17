package com.sergeykotov.adapter.system;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.ExtractionException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class System1 implements System {
    private static final Logger log = Logger.getLogger(System1.class);
    private static final String NAME = "System1";

    private List<Rule> rules = new ArrayList<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<Rule> getRules() {
        log.info("extracting Rule Sets...");
        try {
            Thread.sleep(2_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("Rule Set extraction has been interrupted");
            throw new ExtractionException();
        }
        log.info(rules.size() + " Rule Sets have been extracted");
        return rules;
    }

    @Override
    public boolean createRuleSet(Rule rule) {
        log.info("creating Rule Set " + rule + "...");
        try {
            Thread.sleep(15_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("Rule Set creation has been interrupted");
            return false;
        }
        if (rules.contains(rule)) {
            log.error("failed to create Rule Set " + rule);
            return false;
        }
        rules.add(rule);
        log.info("Rule Set " + rule + " has been created");
        return true;
    }

    @Override
    public boolean deleteRuleSet(Rule rule) {
        log.info("deleting Rule Set " + rule + "...");
        try {
            Thread.sleep(10_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("Rule Set deletion has been interrupted");
            return false;
        }
        boolean deleted = rules.remove(rule);
        if (!deleted) {
            log.error("failed to delete Rule Set " + rule);
            return false;
        }
        log.info("Rule Set " + rule + " has been deleted");
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}