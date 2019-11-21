package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.NotFoundException;
import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.service.AuthorizationService;
import com.sergeykotov.adapter.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rule")
public class RuleController {
    private final TaskQueue taskQueue;
    private final AuthorizationService authorizationService;
    private final RuleService ruleService;

    @Autowired
    public RuleController(TaskQueue taskQueue, AuthorizationService authorizationService, RuleService ruleService) {
        this.taskQueue = taskQueue;
        this.authorizationService = authorizationService;
        this.ruleService = ruleService;
    }

    @GetMapping
    public List<Rule> getRules(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return ruleService.getRules();
    }

    @GetMapping("/id")
    public Rule getRule(@RequestHeader String authorization, @RequestBody @Valid Rule rule) throws NotFoundException {
        authorizationService.authorize(authorization);
        return ruleService.getRule(rule);
    }

    @PostMapping
    public void createRule(@RequestHeader String authorization, @RequestBody @Valid Rule rule) {
        authorizationService.authorize(authorization);
        taskQueue.submitCreateRuleTask(ruleService, rule);
    }

    @PutMapping
    public void updateRule(@RequestHeader String authorization, @RequestBody @Valid Rule rule) {
        authorizationService.authorize(authorization);
        taskQueue.submitUpdateRuleTask(ruleService, rule);
    }

    @DeleteMapping
    public void deleteRule(@RequestHeader String authorization, @RequestBody @Valid Rule rule) {
        authorizationService.authorize(authorization);
        taskQueue.submitDeleteRuleTask(ruleService, rule);
    }
}