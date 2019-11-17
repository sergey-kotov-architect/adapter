package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.IntegrityDto;
import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.domain.RulesDto;
import com.sergeykotov.adapter.system.System;
import com.sergeykotov.adapter.system.System1;
import com.sergeykotov.adapter.system.System2;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RuleService {
    private static final Logger log = Logger.getLogger(RuleService.class);
    private final List<System> systems;

    @Autowired
    public RuleService(System1 system1, System2 system2) {
        List<System> systems = new ArrayList<>();
        systems.add(system1);
        systems.add(system2);
        this.systems = Collections.unmodifiableList(systems);
    }

    public RulesDto getRulesDto() {
        log.info("extracting rules...");
        Map<Rule, List<String>> ruleSetSystemsMap = new HashMap<>();
        for (System system : systems) {
            List<Rule> rules = system.getRules();
            for (Rule rule : rules) {
                ruleSetSystemsMap.putIfAbsent(rule, new ArrayList<>());
                List<String> ruleSetSystems = ruleSetSystemsMap.get(rule);
                ruleSetSystems.add(system.getName());
                ruleSetSystemsMap.put(rule, ruleSetSystems);
            }
        }
        log.info(ruleSetSystemsMap.size() + " rules have been extracted");
        RulesDto rulesDto = new RulesDto();
        rulesDto.setRuleSetSystemsMap(ruleSetSystemsMap);
        return rulesDto;
    }

    public Rule getRule(long id) {
        return null;
    }

    public void create(Rule rule) {
        log.info("creating rule " + rule + "...");
        List<System> affectedSystems = new ArrayList<>(systems.size());
        for (System system : systems) {
            boolean created = system.createRuleSet(rule);
            if (created) {
                affectedSystems.add(system);
                continue;
            }
            log.error("failed to create rule " + rule + " on system " + system);
            for (System affectedSystem : affectedSystems) {
                boolean deleted = affectedSystem.deleteRuleSet(rule);
                if (!deleted) {
                    String message = "integrity has been violated: system %s must not contain rule %s";
                    log.error(String.format(message, affectedSystem, rule));
                }
            }
            return;
        }
        log.info("rule " + rule + " has been created");
    }

    public void update(Rule rule) {

    }

    public void delete(Rule rule) {
        log.info("deleting rule " + rule + "...");
        List<System> affectedSystems = new ArrayList<>(systems.size());
        for (System system : systems) {
            boolean deleted = system.deleteRuleSet(rule);
            if (deleted) {
                affectedSystems.add(system);
                continue;
            }
            log.error("failed to delete rule " + rule + " on system " + system);
            for (System affectedSystem : affectedSystems) {
                boolean created = affectedSystem.createRuleSet(rule);
                if (!created) {
                    String message = "integrity has been violated: system %s must contain rule %s";
                    log.error(String.format(message, affectedSystem, rule));
                }
            }
            return;
        }
        log.info("rule " + rule + " has been deleted");
    }

    public IntegrityDto verifyIntegrity() {
        String time = LocalDateTime.now().toString();
        String message = "the business rules are consistent among the systems";
        IntegrityDto integrityDto = new IntegrityDto();
        integrityDto.setConsistent(true);
        integrityDto.setTime(time);
        integrityDto.setMessage(message);
        return integrityDto;
    }

    public void restoreIntegrity() {

    }
}