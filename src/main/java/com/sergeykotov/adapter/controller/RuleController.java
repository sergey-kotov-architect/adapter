package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.NotFoundException;
import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rule")
public class RuleController {
    private final TaskQueue taskQueue;
    private final RuleService ruleService;

    @Autowired
    public RuleController(TaskQueue taskQueue, RuleService ruleService) {
        this.taskQueue = taskQueue;
        this.ruleService = ruleService;
    }

    @GetMapping
    public List<Rule> getRules() {
        return ruleService.getRules();
    }

    @GetMapping("/{id}")
    public Rule getRule(@PathVariable long id) throws NotFoundException {
        return ruleService.getRule(id);
    }

    @PostMapping
    public void createRule(@RequestBody @Valid Rule rule) {
        taskQueue.submitCreateRuleTask(ruleService, rule);
    }

    @PutMapping
    public void updateRule(@RequestBody @Valid Rule rule) {
        taskQueue.submitUpdateRuleTask(ruleService, rule);
    }

    @DeleteMapping
    public void deleteRule(@RequestBody @Valid Rule rule) {
        taskQueue.submitDeleteRuleTask(ruleService, rule);
    }
}