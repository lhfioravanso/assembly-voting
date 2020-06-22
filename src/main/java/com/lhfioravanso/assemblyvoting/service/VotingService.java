package com.lhfioravanso.assemblyvoting.service;

import com.lhfioravanso.assemblyvoting.dto.VoteRequestDto;
import com.lhfioravanso.assemblyvoting.dto.VoteResponseDto;
import com.lhfioravanso.assemblyvoting.dto.VotingRequestDto;
import com.lhfioravanso.assemblyvoting.dto.VotingResponseDto;

import java.util.List;

public interface VotingService {
    List<VotingResponseDto> listVotings();
    VotingResponseDto getVoting(String id);
    VotingResponseDto createVoting(VotingRequestDto dto);
    VoteResponseDto addVote(VoteRequestDto dto);
}
