package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.domain.IntegrityDto;
import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.domain.RulesDto;
import com.sergeykotov.adapter.exception.ExtractionException;
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
        log.info("extracting rule by ID " + id + "...");
        //TODO: implement rule extraction by ID
        try {
            Thread.sleep(1_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("rule extraction by ID " + id + " has been interrupted");
            throw new ExtractionException();
        }
        log.info("rule has been extracted by ID " + id);
        return null;
    }

    public void create(Rule rule) {
        log.info("creating rule " + rule + "...");
        List<System> affectedSystems = new ArrayList<>(systems.size());
        for (System system : systems) {
            boolean created = system.createRule(rule);
            if (created) {
                affectedSystems.add(system);
                continue;
            }
            log.error("failed to create rule " + rule + " on system " + system);
            for (System affectedSystem : affectedSystems) {
                boolean deleted = affectedSystem.deleteRule(rule);
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
        log.info("updating rule " + rule + "...");
        Map<System, Rule> affectedSystems = new HashMap<>(systems.size() * 2);
        for (System system : systems) {
            boolean updated = system.updateRule(rule);
            if (updated) {
                Rule previousRule = getRule(rule.getId());
                affectedSystems.put(system, previousRule);
                continue;
            }
            log.error("failed to update rule " + rule + " on system " + system);
            for (Map.Entry<System, Rule> entry : affectedSystems.entrySet()) {
                System affectedSystem = entry.getKey();
                Rule previousRule = entry.getValue();
                boolean restored = affectedSystem.updateRule(previousRule);
                if (!restored) {
                    String message = "integrity has been violated: system %s has rule %s in invalid state";
                    log.error(String.format(message, affectedSystem, rule));
                }
            }
            return;
        }
        log.info("rule " + rule + " has been updated");
    }

    public void delete(Rule rule) {
        log.info("deleting rule " + rule + "...");
        List<System> affectedSystems = new ArrayList<>(systems.size());
        for (System system : systems) {
            boolean deleted = system.deleteRule(rule);
            if (deleted) {
                affectedSystems.add(system);
                continue;
            }
            log.error("failed to delete rule " + rule + " on system " + system);
            for (System affectedSystem : affectedSystems) {
                boolean created = affectedSystem.createRule(rule);
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
        log.info("verifying integrity...");
        //TODO: implement integrity verification
        try {
            Thread.sleep(3_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("integrity verification has been interrupted");
            throw new ExtractionException();
        }
        log.info("integrity has been verified");

        String time = LocalDateTime.now().toString();
        String message = "the business rules are consistent among the systems";
        IntegrityDto integrityDto = new IntegrityDto();
        integrityDto.setConsistent(true);
        integrityDto.setTime(time);
        integrityDto.setMessage(message);
        return integrityDto;
    }

    public void restoreIntegrity() {
        log.info("restoring integrity...");
        //TODO: implement integrity restoration
        try {
            Thread.sleep(15_000L); //latency simulation
        } catch (InterruptedException e) {
            log.error("integrity extraction has been interrupted");
            return;
        }
        log.info("integrity has been restored");
    }
}