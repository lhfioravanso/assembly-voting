package com.lhfioravanso.assemblyvoting.service;

import com.lhfioravanso.assemblyvoting.dto.VotingResultResponseDto;

public interface MessagingService {
    void send(VotingResultResponseDto votingResult);
}
