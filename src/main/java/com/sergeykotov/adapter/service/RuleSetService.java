package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.RuleSet;
import com.sergeykotov.adapter.system.System;
import com.sergeykotov.adapter.system.System1;
import com.sergeykotov.adapter.system.System2;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RuleSetService {
    private static final Logger log = Logger.getLogger(RuleSetService.class);
    private final List<System> systems;

    @Autowired
    public RuleSetService(System1 system1, System2 system2) {
        List<System> systems = new ArrayList<>();
        systems.add(system1);
        systems.add(system2);
        this.systems = Collections.unmodifiableList(systems);
    }

    public Map<RuleSet, List<String>> getRuleSets() {
        log.info("extracting Rule Sets...");
        Map<RuleSet, List<String>> ruleSetSystemsMap = new HashMap<>();
        for (System system : systems) {
            List<RuleSet> ruleSets = system.getRuleSets();
            for (RuleSet ruleSet : ruleSets) {
                ruleSetSystemsMap.putIfAbsent(ruleSet, new ArrayList<>());
                List<String> ruleSetSystems = ruleSetSystemsMap.get(ruleSet);
                ruleSetSystems.add(system.getName());
                ruleSetSystemsMap.put(ruleSet, ruleSetSystems);
            }
        }
        log.info(ruleSetSystemsMap.size() + " Rule Sets have been extracted");
        return ruleSetSystemsMap;
    }

    public void create(RuleSet ruleSet) {
        log.info("creating Rule Set " + ruleSet + "...");
        for (System system : systems) {
            boolean created = system.createRuleSet(ruleSet);
            if (!created) {
                log.error("failed to create Rule Set " + ruleSet + " on system " + system);
                //TODO: delete created Rule Set on previous systems
                return;
            }
        }
        log.info("Rule Set " + ruleSet + " has been created");
    }

    public void delete(RuleSet ruleSet) {
        log.info("deleting Rule Set " + ruleSet + "...");
        for (System system : systems) {
            boolean deleted = system.deleteRuleSet(ruleSet);
            if (!deleted) {
                log.error("failed to delete Rule Set " + ruleSet + " on system " + system);
                //TODO: restore deleted Rule Set on previous systems
                return;
            }
        }
        log.info("Rule Set " + ruleSet + " has been deleted");
    }
}