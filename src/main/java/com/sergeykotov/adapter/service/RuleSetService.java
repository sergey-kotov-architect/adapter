package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.RuleSet;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RuleSetService {
    private static final Logger log = Logger.getLogger(RuleSetService.class);
    private static final List<RuleSet> ruleSets = new CopyOnWriteArrayList<>();

    public List<RuleSet> getRuleSets() {
        log.info("extracting Rule Sets...");
        try {
            Thread.sleep(2_000L);
        } catch (InterruptedException e) {
            log.info("Rule Sets extraction has been interrupted");
            return Collections.emptyList();
        }
        log.info("RuleSets have been extraction");
        return ruleSets;
    }

    public void create(RuleSet ruleSet) {
        log.info("creating Rule Set " + ruleSet + "...");
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            log.info("Rule Set " + ruleSet + " creation has been interrupted");
            return;
        }
        if (ruleSets.contains(ruleSet)) {
            log.error("failed to create Rule Set " + ruleSet);
            return;
        }
        ruleSets.add(ruleSet);
        log.info("Rule Set " + ruleSet + " has been created");
    }

    public void delete(RuleSet ruleSet) {
        log.info("deleting Rule Set " + ruleSet + "...");
        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            log.info("Rule Set " + ruleSet + " deletion has been interrupted");
            return;
        }
        boolean deleted = ruleSets.remove(ruleSet);
        if (deleted) {
            log.info("Rule Set " + ruleSet + " has been deleted");
        } else {
            log.info("failed to delete Rule Set " + ruleSet);
        }
    }
}