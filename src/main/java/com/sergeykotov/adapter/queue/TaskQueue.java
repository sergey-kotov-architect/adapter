package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.domain.RuleSet;
import com.sergeykotov.adapter.exception.TaskQueueException;
import com.sergeykotov.adapter.service.RuleSetService;
import com.sergeykotov.adapter.task.CreateRuleSetTask;
import com.sergeykotov.adapter.task.DeleteRuleSetTask;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskDto;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Component
public class TaskQueue {
    private static final Logger log = Logger.getLogger(TaskQueue.class);
    private static final int CAPACITY = 10;

    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>(CAPACITY);

    public TaskQueue() {
        new TaskQueueProcessing(queue).start();
    }

    public TaskQueueDto getTaskQueueDto() {
        List<TaskDto> tasks = queue.stream().map(Task::getTaskDto).collect(Collectors.toList());
        TaskQueueDto taskQueueDto = new TaskQueueDto();
        taskQueueDto.setCapacity(CAPACITY);
        taskQueueDto.setSize(tasks.size());
        taskQueueDto.setTasks(tasks);
        return taskQueueDto;
    }

    public void submitCreateRuleSetTask(RuleSetService ruleSetService, RuleSet ruleSet) {
        Task task = new CreateRuleSetTask(ruleSetService, ruleSet);
        boolean accepted = queue.offer(task);
        if (!accepted) {
            log.error("failed to submit a task to create Rule Set " + ruleSet);
            throw new TaskQueueException();
        }
        log.info("task to create Rule Set " + ruleSet + " has been submitted, queue size " + queue.size());
    }

    public void submitDeleteRuleSetTask(RuleSetService ruleSetService, RuleSet ruleSet) {
        Task task = new DeleteRuleSetTask(ruleSetService, ruleSet);
        boolean accepted = queue.offer(task);
        if (!accepted) {
            log.error("failed to submit a task to delete Rule Set " + ruleSet);
            throw new TaskQueueException();
        }
        log.info("task to delete Rule Set " + ruleSet + " has been submitted, queue size " + queue.size());
    }
}