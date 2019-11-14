package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.domain.RuleSet;
import com.sergeykotov.adapter.exception.TaskQueueException;
import com.sergeykotov.adapter.service.RuleSetService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Component
public class TaskQueue {
    private static final Logger log = Logger.getLogger(TaskQueue.class);
    private static final int CAPACITY = 10;

    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>(CAPACITY);

    public TaskQueue() {
        TaskQueueProcessing taskQueueProcessing = new TaskQueueProcessing(queue);
        taskQueueProcessing.start();
    }

    public TaskQueueDto getTaskQueueDto() {
        TaskQueueDto taskQueueDto = new TaskQueueDto();
        taskQueueDto.setCapacity(CAPACITY);
        taskQueueDto.setTasks(queue.stream().map(Task::getName).map(TaskDto::new).collect(Collectors.toList()));
        return taskQueueDto;
    }

    public void submitRuleSetCreation(RuleSet ruleSet, RuleSetService ruleSetService) {
        Task task = new CreateRuleSetTask(ruleSet, ruleSetService);
        boolean accepted = queue.offer(task);
        if (!accepted) {
            log.error("failed to submit a task to create Rule Set " + ruleSet);
            throw new TaskQueueException();
        }
        log.info("task to create Rule Set " + ruleSet + " has been submitted, queue size " + queue.size());
    }

    public void submitRuleSetDeletion(RuleSet ruleSet, RuleSetService ruleSetService) {
        Task task = new DeleteRuleSetTask(ruleSet, ruleSetService);
        boolean accepted = queue.offer(task);
        if (!accepted) {
            log.error("failed to submit a task to delete Rule Set " + ruleSet);
            throw new TaskQueueException();
        }
        log.info("task to delete Rule Set " + ruleSet + " has been submitted, queue size " + queue.size());
    }
}