package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.domain.IntegrityDto;
import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.domain.RulesDto;
import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.queue.TaskQueueDto;
import com.sergeykotov.adapter.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class Controller {
    private final TaskQueue taskQueue;
    private final RuleService ruleService;

    @Autowired
    public Controller(TaskQueue taskQueue, RuleService ruleService) {
        this.taskQueue = taskQueue;
        this.ruleService = ruleService;
    }

    @GetMapping("/rule")
    public RulesDto getRuleSets() {
        return ruleService.getRulesDto();
    }

    @GetMapping("/rule/{id}")
    public Rule getRule(@PathVariable long id) {
        return ruleService.getRule(id);
    }

    @PostMapping("/rule")
    public void createRule(@RequestBody @Valid Rule rule) {
        taskQueue.submitCreateRuleTask(ruleService, rule);
    }

    @PutMapping("/rule")
    public void updateRule(@RequestBody @Valid Rule rule) {
        taskQueue.submitUpdateRuleTask(ruleService, rule);
    }

    @DeleteMapping("/rule")
    public void deleteRule(@RequestBody @Valid Rule rule) {
        taskQueue.submitDeleteRuleTask(ruleService, rule);
    }

    @GetMapping("/integrity")
    public IntegrityDto verifyIntegrity() {
        return ruleService.verifyIntegrity();
    }

    @PostMapping("/integrity")
    public void restoreIntegrity() {
        taskQueue.submitRestoreIntegrityTask(ruleService);
    }

    @GetMapping("/queue")
    public TaskQueueDto getTaskQueueDto() {
        return taskQueue.getTaskQueueDto();
    }
}