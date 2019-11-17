package com.sergeykotov.adapter.domain;

public class IntegrityDto {
    private boolean consistent;
    private String time;
    private String message;

    public IntegrityDto() {
    }

    public boolean isConsistent() {
        return consistent;
    }

    public void setConsistent(boolean consistent) {
        this.consistent = consistent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}