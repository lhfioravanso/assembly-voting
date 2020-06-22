package com.lhfioravanso.assemblyvoting.dto;

public class VoteResponseDto {
    private boolean success;

    public VoteResponseDto(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
