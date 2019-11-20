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
    public List<Rule> getRules(@RequestParam String authorization) {
        authorizationService.authorize(authorization);
        return ruleService.getRules();
    }

    @GetMapping("/{id}")
    public Rule getRule(@PathVariable long id, @RequestParam String authorization) throws NotFoundException {
        authorizationService.authorize(authorization);
        return ruleService.getRule(id);
    }

    @PostMapping
    public void createRule(@RequestParam String authorization, @RequestBody @Valid Rule rule) {
        authorizationService.authorize(authorization);
        taskQueue.submitCreateRuleTask(ruleService, rule);
    }

    @PutMapping("/{id}")
    public void updateRule(@PathVariable long id, @RequestParam String authorization, @RequestBody @Valid Rule rule) {
        authorizationService.authorize(authorization);
        taskQueue.submitUpdateRuleTask(ruleService, id, rule);
    }

    @DeleteMapping("/{id}")
    public void deleteRule(@PathVariable long id, @RequestParam String authorization) {
        authorizationService.authorize(authorization);
        taskQueue.submitDeleteRuleTask(ruleService, id);
    }
}