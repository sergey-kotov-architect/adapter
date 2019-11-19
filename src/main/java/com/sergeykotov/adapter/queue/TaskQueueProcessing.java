package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.task.Task;
import com.sergeykotov.adapter.task.TaskDto;
import com.sergeykotov.adapter.task.TaskResult;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TaskQueueProcessing extends Thread {
    private static final Logger log = Logger.getLogger(TaskQueueProcessing.class);
    private static final String NAME = "task-queue-processing";
    private static final int TASK_RESULTS_CAPACITY = Integer.MAX_VALUE;

    private final BlockingQueue<Task> queue;
    private final List<TaskResult> taskResults;
    private Task task;

    public TaskQueueProcessing(BlockingQueue<Task> queue, List<TaskResult> taskResults) {
        this.queue = queue;
        this.taskResults = taskResults;
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
            if (taskResults.size() == TASK_RESULTS_CAPACITY) {
                taskResults.remove(0);
            }
            taskResults.add(taskResult);
            task = null;
        }
        log.error("task queue processing has been interrupted");
    }
}