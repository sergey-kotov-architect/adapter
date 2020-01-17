package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.dao.TaskResultDao;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskDto;
import com.sergeykotov.adapter.task.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class TaskConsumer extends Thread {
    private static final Logger log = LoggerFactory.getLogger(TaskConsumer.class);
    private static final String NAME = "task-consumer";

    private final BlockingQueue<Task> taskQueue;
    private final TaskResultDao taskResultDao;
    private Task task;

    public TaskConsumer(BlockingQueue<Task> taskQueue, TaskResultDao taskResultDao) {
        this.taskQueue = taskQueue;
        this.taskResultDao = taskResultDao;
        setName(NAME);
    }

    public Optional<TaskDto> getExecutingTaskDto() {
        return task == null ? Optional.empty() : Optional.of(task.getTaskDto());
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            log.info("waiting for a new task in the taskQueue...");
            try {
                task = taskQueue.take();
            } catch (InterruptedException e) {
                log.error("task taskQueue processing has been interrupted");
                return;
            }
            log.info("a new task " + task + " has been taken from the taskQueue, taskQueue size " + taskQueue.size());
            LocalDateTime start = LocalDateTime.now();

            TaskResult taskResult = task.execute();

            LocalDateTime end = LocalDateTime.now();
            log.info("task " + task + " has been executed, taskQueue size " + taskQueue.size());
            taskResult.setStartTime(start.toString());
            taskResult.setEndTime(end.toString());
            taskResult.setTask(task.getTaskDto());
            boolean saved = false;
            try {
                saved = taskResultDao.save(taskResult);
            } catch (SQLException e) {
                String err = ", error code: " + e.getErrorCode();
                log.error("failed to save task " + task + " result: " + taskResult.isSucceeded() + err, e);
            }
            if (!saved) {
                log.error("failed to save task " + task + " result: " + taskResult.isSucceeded());
            }
            task = null;
        }
        log.error("task taskQueue processing has been interrupted");
    }
}