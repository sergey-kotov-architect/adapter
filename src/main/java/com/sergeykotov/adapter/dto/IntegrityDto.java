package com.sergeykotov.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IntegrityDto {
    private boolean verified;
    private String verificationStartTime;
    private List<String> notes;

    public IntegrityDto() {
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @JsonProperty("verification_start_time")
    public String getVerificationStartTime() {
        return verificationStartTime;
    }

    public void setVerificationStartTime(String verificationStartTime) {
        this.verificationStartTime = verificationStartTime;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}