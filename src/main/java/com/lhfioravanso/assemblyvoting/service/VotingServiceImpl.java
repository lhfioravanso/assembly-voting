package com.lhfioravanso.assemblyvoting.service;

import com.lhfioravanso.assemblyvoting.dto.*;
import com.lhfioravanso.assemblyvoting.entity.*;
import com.lhfioravanso.assemblyvoting.exception.BusinessException;
import com.lhfioravanso.assemblyvoting.exception.NotFoundException;
import com.lhfioravanso.assemblyvoting.integration.CpfService;
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
    private final ModelMapper modelMapper;
    private final Environment environment;
    private final CpfService cpfService;

    @Autowired
    public VotingServiceImpl(VotingRepository votingRepository, ModelMapper modelMapper,
                             AgendaRepository agendaRepository, Environment environment, CpfService cpfService) {
        this.votingRepository = votingRepository;
        this.agendaRepository = agendaRepository;
        this.modelMapper = modelMapper;
        this.environment = environment;
        this.cpfService = cpfService;
    }

    @Override
    public List<VotingResponseDto> listVotings() {
        List<Voting> votingList = this.votingRepository.findAll();

        return votingList.stream().map(
                voting -> modelMapper.map(voting, VotingResponseDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public VotingResponseDto getVoting(String id) {
        Voting voting = findVoting(id);
        return modelMapper.map(voting, VotingResponseDto.class);
    }

    @Override
    public VotingResponseDto createVoting(VotingRequestDto dto) {
        Agenda agenda = this.agendaRepository.findById(new ObjectId(dto.getAgendaId())).
                orElseThrow(() -> new NotFoundException("Agenda not found."));

        Integer minutesToExpiration = dto.getMinutesToExpiration();
        if (minutesToExpiration == null || minutesToExpiration <= 0)
            minutesToExpiration = (Integer.parseInt(Objects.requireNonNull(environment.getProperty("default.expiration.minutes"))));

        Voting voting = new Voting(agenda, minutesToExpiration);
        voting = this.votingRepository.insert(voting);

        return modelMapper.map(voting, VotingResponseDto.class);
    }

    @Override
    public VoteResponseDto addVote(VoteRequestDto dto) {
        Voting voting = findVoting(dto.getVotingId());

        if (voting.isExpired())
            throw new BusinessException("Voting already expired.");

        if (voting.cpfAlreadyVoted(dto.getCpf()))
            throw new BusinessException("Associated with CPF ("+dto.getCpf()+") already voted.");

        if (cpfService.isAbleToVote(dto.getCpf()))
            throw new BusinessException("CPF is unable to vote.");

        Vote vote = new Vote(dto.getCpf(), dto.getAnswer());
        voting.addVote(vote);
        votingRepository.save(voting);

        return new VoteResponseDto(true);
    }

    @Override
    public VotingResultResponseDto getVotingResult(String id) {
        Voting voting = findVoting(id);

        if (!voting.isExpired())
            throw new BusinessException("Voting still open, it will close at " + voting.getExpirationDate().toString());

        List<Vote> votes = voting.getVotes();

        VoteCount voteCount = new VoteCount(
                votes.stream().filter(vote -> vote.getAnswer().equals(Answer.YES)).count(),
                votes.stream().filter(vote -> vote.getAnswer().equals(Answer.NO)).count()
        );

        VotingResultResponseDto resultResponseDto = new VotingResultResponseDto();
        resultResponseDto.setAgenda(modelMapper.map(voting.getAgenda(), AgendaResponseDto.class));
        resultResponseDto.setVoteCount(voteCount);

        return resultResponseDto;
    }

    private Voting findVoting(String id){
       return this.votingRepository.findById(new ObjectId(id)).
                orElseThrow(() -> new NotFoundException("Voting not found."));
    }
}
