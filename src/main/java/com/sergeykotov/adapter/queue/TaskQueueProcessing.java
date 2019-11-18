package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.task.Task;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;

public class TaskQueueProcessing extends Thread {
    private static final Logger log = Logger.getLogger(TaskQueueProcessing.class);
    private static final String NAME = "task-queue-processing";

    private final BlockingQueue<Task> queue;
    private Task task;

    public TaskQueueProcessing(BlockingQueue<Task> queue) {
        this.queue = queue;
        setName(NAME);
        setDaemon(true);
    }

    public Task getTask() {
        return task;
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
            log.info("a new task " + task + " has been taken from the queue");
            task.execute();
            task = null;
        }
        log.error("task queue processing has been interrupted");
    }
}