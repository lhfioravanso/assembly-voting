package com.lhfioravanso.assemblyvoting.service;

import com.lhfioravanso.assemblyvoting.dto.*;
import com.lhfioravanso.assemblyvoting.entity.*;
import com.lhfioravanso.assemblyvoting.exception.BusinessException;
import com.lhfioravanso.assemblyvoting.exception.NotFoundException;
import com.lhfioravanso.assemblyvoting.repository.AgendaRepository;
import com.lhfioravanso.assemblyvoting.repository.VotingRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VotingServiceImpl implements VotingService{

    private final VotingRepository votingRepository;
    private final AgendaRepository agendaRepository;
    private final ModelMapper votingMapper;
    private final ModelMapper agendaMapper;
    private final Environment environment;

    @Autowired
    public VotingServiceImpl(VotingRepository votingRepository, ModelMapper votingMapper, ModelMapper agendaMapper, AgendaRepository agendaRepository, Environment environment) {
        this.votingRepository = votingRepository;
        this.agendaRepository = agendaRepository;
        this.votingMapper = votingMapper;
        this.agendaMapper = agendaMapper;
        this.environment = environment;
    }

    @Override
    public List<VotingResponseDto> listVotings() {
        List<Voting> votingList = this.votingRepository.findAll();

        return votingList.stream().map(
                voting -> votingMapper.map(voting, VotingResponseDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public VotingResponseDto getVoting(String id) {
        Voting voting = findVoting(id);
        return votingMapper.map(voting, VotingResponseDto.class);
    }

    @Override
    public VotingResponseDto createVoting(VotingRequestDto dto) {
        Agenda agenda = this.agendaRepository.findById(new ObjectId(dto.getAgendaId())).
                orElseThrow(() -> new NotFoundException("Agenda not found."));

        if (dto.getMinutesToExpiration() == null)
            dto.setMinutesToExpiration(Integer.parseInt(Objects.requireNonNull(environment.getProperty("default.expiration.minutes"))));

        Voting voting = new Voting(agenda, dto.getMinutesToExpiration());
        voting = this.votingRepository.insert(voting);

        return votingMapper.map(voting, VotingResponseDto.class);
    }

    @Override
    public VoteResponseDto addVote(VoteRequestDto dto) {
        Voting voting = findVoting(dto.getVotingId());

        //TODO: validar se cpf pode votar #bonus..
        if (voting.isExpired())
            throw new BusinessException("Voting already expired.");

        if (voting.cpfAlreadyVoted(dto.getCpf()))
            throw new BusinessException("Associated with CPF ("+dto.getCpf()+") already voted.");

        Vote vote = new Vote(dto.getCpf(), dto.getAnswer());
        voting.addVote(vote);
        votingRepository.save(voting);

        return new VoteResponseDto(true);
    }

    @Override
    public VotingResultResponseDto getVotingResult(String id) {
        Voting voting = findVoting(id);
        List<Vote> votes = voting.getVotes();

        VoteCount voteCount = new VoteCount(
                votes.stream().filter(vote -> vote.getAnswer().equals(Answer.YES)).count(),
                votes.stream().filter(vote -> vote.getAnswer().equals(Answer.NO)).count()
        );

        VotingResultResponseDto resultResponseDto = new VotingResultResponseDto();
        resultResponseDto.setAgenda(agendaMapper.map(voting.getAgenda(), AgendaResponseDto.class));
        resultResponseDto.setDecision(voteCount.getYes()>voteCount.getNo()? Answer.YES : Answer.NO);
        resultResponseDto.setVoteCount(voteCount);

        return resultResponseDto;
    }

    private Voting findVoting(String id){
       return this.votingRepository.findById(new ObjectId(id)).
                orElseThrow(() -> new NotFoundException("Voting not found."));
    }
}
