package com.lhfioravanso.assemblyvoting.service;

import com.lhfioravanso.assemblyvoting.dto.VotingRequestDto;
import com.lhfioravanso.assemblyvoting.dto.VotingResponseDto;
import com.lhfioravanso.assemblyvoting.entity.Agenda;
import com.lhfioravanso.assemblyvoting.entity.Voting;
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
    private final Environment environment;

    @Autowired
    public VotingServiceImpl(VotingRepository votingRepository, ModelMapper votingMapper, AgendaRepository agendaRepository, Environment environment) {
        this.votingRepository = votingRepository;
        this.agendaRepository = agendaRepository;
        this.votingMapper = votingMapper;
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
        Voting voting = this.votingRepository.findById(new ObjectId(id)).
                orElseThrow(() -> new NotFoundException("Voting not found."));

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
}
