package com.lhfioravanso.assemblyvoting.entity;

public class VoteCount {
    private Long yes;
    private Long no;

    public VoteCount(Long yes, Long no) {
        this.yes = yes;
        this.no = no;
    }

    public Long getYes() {
        return yes;
    }

    public void setYes(Long yes) {
        this.yes = yes;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }
}
