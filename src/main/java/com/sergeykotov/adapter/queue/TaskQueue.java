package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.dao.TaskResultDao;
import com.sergeykotov.adapter.domain.Rule;
import com.sergeykotov.adapter.exception.DatabaseException;
import com.sergeykotov.adapter.exception.ExtractionException;
import com.sergeykotov.adapter.exception.InvalidInputException;
import com.sergeykotov.adapter.exception.TaskQueueException;
import com.sergeykotov.adapter.service.IntegrityService;
import com.sergeykotov.adapter.service.RuleService;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskDto;
import com.sergeykotov.adapter.task.TaskResult;
import com.sergeykotov.adapter.task.implementation.CreateRuleTask;
import com.sergeykotov.adapter.task.implementation.DeleteRuleTask;
import com.sergeykotov.adapter.task.implementation.RestoreIntegrityTask;
import com.sergeykotov.adapter.task.implementation.UpdateRuleTask;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Component
public class TaskQueue {
    private static final Logger log = Logger.getLogger(TaskQueue.class);
    private static final int CAPACITY = Integer.MAX_VALUE;

    private final TaskResultDao taskResultDao;
    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>(CAPACITY);
    private final TaskQueueProcessing taskQueueProcessing;

    @Autowired
    public TaskQueue(TaskResultDao taskResultDao) {
        this.taskResultDao = taskResultDao;
        taskQueueProcessing = new TaskQueueProcessing(queue, taskResultDao);
        taskQueueProcessing.start();
    }

    public TaskQueueDto getTaskQueueDto() {
        List<TaskDto> tasks = queue.stream().map(Task::getTaskDto).collect(Collectors.toList());
        TaskDto executingTaskDto = taskQueueProcessing.getExecutingTaskDto();

        TaskQueueDto taskQueueDto = new TaskQueueDto();
        taskQueueDto.setCapacity(CAPACITY);
        taskQueueDto.setSize(tasks.size());
        taskQueueDto.setExecutingTask(executingTaskDto);
        taskQueueDto.setTasks(tasks);
        return taskQueueDto;
    }

    public List<TaskResult> getTaskResults() {
        log.info("extracting task results...");
        List<TaskResult> taskResults;
        try {
            taskResults = taskResultDao.extract();
        } catch (SQLException e) {
            log.error("failed to extract task results", e);
            throw new ExtractionException(e);
        }
        log.info(taskResults.size() + " task results have been extracted");
        return taskResults;
    }

    public int deleteTaskResults(String dateTime) {
        log.info("deleting task results earlier than " + dateTime + "...");
        try {
            dateTime = LocalDateTime.parse(dateTime).toString();
        } catch (Exception e) {
            log.error("failed to parse input datetime " + dateTime, e);
            throw new InvalidInputException();
        }
        int count;
        try {
            count = taskResultDao.delete(dateTime);
        } catch (SQLException e) {
            log.error("failed to delete task results", e);
            throw new DatabaseException();
        }
        log.info(count + " task results have been deleted");
        return count;
    }

    public void submitCreateRuleTask(RuleService ruleService, Rule rule) {
        Task task = new CreateRuleTask(ruleService, rule);
        submitTask(task);
    }

    public void submitUpdateRuleTask(RuleService ruleService, long id, Rule rule) {
        Task task = new UpdateRuleTask(ruleService, id, rule);
        submitTask(task);
    }

    public void submitDeleteRuleTask(RuleService ruleService, long id) {
        Task task = new DeleteRuleTask(ruleService, id);
        submitTask(task);
    }

    public void submitRestoreIntegrityTask(IntegrityService integrityService, List<Rule> rules) {
        Task task = new RestoreIntegrityTask(integrityService, rules);
        submitTask(task);
    }

    private void submitTask(Task task) {
        boolean accepted = queue.offer(task);
        if (!accepted) {
            log.error("failed to submit task " + task);
            throw new TaskQueueException();
        }
        log.info("task " + task + " has been submitted, queue size " + queue.size());
    }
}