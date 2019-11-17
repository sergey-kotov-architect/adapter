package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.task.Task;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;

public class TaskQueueProcessing extends Thread {
    private static final Logger log = Logger.getLogger(TaskQueueProcessing.class);
    private static final String NAME = "task-queue-processing";

    private final BlockingQueue<Task> queue;

    public TaskQueueProcessing(BlockingQueue<Task> queue) {
        this.queue = queue;
        setName(NAME);
        setDaemon(true);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            log.info("waiting for a new task in the queue...");
            Task task;
            try {
                task = queue.take();
            } catch (InterruptedException e) {
                log.error("task queue processing has been interrupted");
                return;
            }
            log.info("a new task " + task + " has been taken from the queue");
            task.execute();
        }
        log.error("task queue processing has been interrupted");
    }
}