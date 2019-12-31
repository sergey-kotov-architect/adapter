package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.dao.TaskResultDao;
import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskDto;
import com.sergeykotov.adapter.task.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;

public class TaskQueueProcessing extends Thread {
    private static final Logger log = LoggerFactory.getLogger(TaskQueueProcessing.class);
    private static final String NAME = "task-queue-processing";

    private final BlockingQueue<Task> queue;
    private final TaskResultDao taskResultDao;
    private Task task;

    public TaskQueueProcessing(BlockingQueue<Task> queue, TaskResultDao taskResultDao) {
        this.queue = queue;
        this.taskResultDao = taskResultDao;
        setName(NAME);
        setDaemon(true);
    }

    public TaskDto getExecutingTaskDto() {
        return task == null ? null : task.getTaskDto();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            log.info("waiting for a new task in the queue...");
            try {
                task = queue.take();
            } catch (InterruptedException e) {
                log.error("task queue processing has been interrupted");
                return;
            }
            log.info("a new task " + task + " has been taken from the queue, queue size " + queue.size());
            LocalDateTime start = LocalDateTime.now();

            TaskResult taskResult = task.execute();

            LocalDateTime end = LocalDateTime.now();
            log.info("task " + task + " has been executed, queue size " + queue.size());
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
        log.error("task queue processing has been interrupted");
    }
}