package com.sergeykotov.adapter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "request queue is full")
public class TaskQueueException extends RuntimeException {
}