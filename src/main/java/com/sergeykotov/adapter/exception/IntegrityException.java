package com.sergeykotov.adapter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Rule Sets are not consistent on systems")
public class IntegrityException extends RuntimeException {
}