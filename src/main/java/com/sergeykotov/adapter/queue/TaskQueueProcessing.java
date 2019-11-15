package com.sergeykotov.adapter.queue;

import com.sergeykotov.adapter.task.Task;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;

public class TaskQueueProcessing extends Thread {
    private static final Logger log = Logger.getLogger(TaskQueueProcessing.class);
    private final BlockingQueue<Task> queue;

    public TaskQueueProcessing(BlockingQueue<Task> queue) {
        this.queue = queue;
        setName("task-queue-processing");
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            Task task;
            try {
                task = queue.take();
            } catch (InterruptedException e) {
                log.error("execution of tasks from the queue has been interrupted");
                return;
            }
            task.execute();
        }
    }
}