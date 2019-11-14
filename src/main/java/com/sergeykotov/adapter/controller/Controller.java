package com.sergeykotov.adapter.controller;

import com.sergeykotov.adapter.domain.RuleSet;
import com.sergeykotov.adapter.queue.TaskQueue;
import com.sergeykotov.adapter.queue.TaskQueueDto;
import com.sergeykotov.adapter.service.RuleSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class Controller {
    private final TaskQueue taskQueue;
    private final RuleSetService ruleSetService;

    @Autowired
    public Controller(TaskQueue taskQueue, RuleSetService ruleSetService) {
        this.taskQueue = taskQueue;
        this.ruleSetService = ruleSetService;
    }

    @GetMapping("/task_queue")
    public TaskQueueDto getTaskQueue() {
        return taskQueue.getTaskQueueDto();
    }

    @GetMapping("/rule_set")
    public Map<RuleSet, List<String>> getRuleSets() {
        return ruleSetService.getRuleSets();
    }

    @PostMapping("/rule_set")
    public void createRuleSet(@RequestBody @Valid RuleSet ruleSet) {
        taskQueue.submitRuleSetCreation(ruleSet, ruleSetService);
    }

    @DeleteMapping("/rule_set")
    public void deleteRuleSet(@RequestBody @Valid RuleSet ruleSet) {
        taskQueue.submitRuleSetDeletion(ruleSet, ruleSetService);
    }
}