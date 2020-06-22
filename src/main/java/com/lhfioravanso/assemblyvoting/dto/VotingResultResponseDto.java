package com.lhfioravanso.assemblyvoting.dto;

import com.lhfioravanso.assemblyvoting.entity.Answer;
import com.lhfioravanso.assemblyvoting.entity.VoteCount;

public class VotingResultResponseDto {
    private AgendaResponseDto agenda;
    private Answer decision;
    private VoteCount voteCount;

    public AgendaResponseDto getAgenda() {
        return agenda;
    }

    public void setAgenda(AgendaResponseDto agenda) {
        this.agenda = agenda;
    }

    public Answer getDecision() {
        return decision;
    }

    public void setDecision(Answer decision) {
        this.decision = decision;
    }

    public VoteCount getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(VoteCount voteCount) {
        this.voteCount = voteCount;
    }
}
