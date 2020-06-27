package com.lhfioravanso.assemblyvoting.service;

import com.lhfioravanso.assemblyvoting.dto.*;
import com.lhfioravanso.assemblyvoting.entity.Agenda;
import com.lhfioravanso.assemblyvoting.entity.Answer;
import com.lhfioravanso.assemblyvoting.entity.Vote;
import com.lhfioravanso.assemblyvoting.entity.Voting;
import com.lhfioravanso.assemblyvoting.exception.BusinessException;
import com.lhfioravanso.assemblyvoting.exception.NotFoundException;
import com.lhfioravanso.assemblyvoting.integration.CpfService;
import com.lhfioravanso.assemblyvoting.repository.AgendaRepository;
import com.lhfioravanso.assemblyvoting.repository.VotingRepository;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class VotingServiceTest {

    @Mock
    public VotingRepository votingRepository;

    @Mock
    public AgendaRepository agendaRepository;

    @Mock
    public CpfService cpfService;

    @Mock
    public ModelMapper modelMapper;

    @InjectMocks
    public VotingServiceImpl votingService;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(votingService, "modelMapper", new ModelMapper());
    }

    @Test
    public void shouldReturnZeroVotings() {
        List<VotingResponseDto> resp = votingService.listVotings();
        assertEquals(0, resp.size());
    }

    @Test
    public void shouldReturnVotings() {
        List<Voting> votings = new ArrayList<>();
        votings.add(new Voting());
        votings.add(new Voting());

        Mockito.when(votingRepository.findAll()).thenReturn(votings);

        List<VotingResponseDto> resp = votingService.listVotings();
        assertEquals(2, resp.size());
    }

    @Test
    public void shouldReturnOneVoting() {
        ObjectId id = new ObjectId();
        Voting voting = new Voting();
        voting.setId(id);

        Mockito.when(votingRepository.findById(id)).thenReturn(java.util.Optional.of(voting));

        VotingResponseDto resp = votingService.getVoting(id.toHexString());
        assertEquals(id.toHexString(), resp.getId());
    }

    @Test
    public void shouldCreateVoting() {
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("test");

        Voting voting = new Voting(agenda, 10);
        voting.setId(id);

        Mockito.when(agendaRepository.findById(id)).thenReturn(java.util.Optional.of(agenda));
        Mockito.when(votingRepository.insert(new Voting(agenda, 10))).thenReturn(voting);

        VotingRequestDto req = new VotingRequestDto();
        req.setAgendaId(voting.getId().toHexString());
        req.setMinutesToExpiration(10);
        VotingResponseDto resp = votingService.createVoting(req);

        assertEquals(id.toHexString(), resp.getId());
    }

    @Test(expected = NotFoundException.class)
    public void shouldNotCreateVotingWhenAgendaNotExists() {
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("test");

        Voting voting = new Voting(agenda, 10);
        voting.setId(id);

        VotingRequestDto req = new VotingRequestDto();
        req.setAgendaId(voting.getId().toHexString());

        votingService.createVoting(req);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundException(){
        ObjectId id = new ObjectId();
        Mockito.when(votingRepository.findById(id)).thenThrow(new NotFoundException("Voting not found."));

        votingService.getVoting(id.toHexString());
    }

    @Test
    public void shouldVote(){
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("test");

        Voting voting = new Voting(agenda, 10);
        Mockito.when(votingRepository.findById(id)).thenReturn(java.util.Optional.of(voting));

        Voting voting2 = new Voting(agenda, 10);
        voting2.setId(id);

        Vote vote = new Vote("123", Answer.NO);
        voting2.addVote(vote);

        Mockito.when(votingRepository.save(voting)).thenReturn(voting2);

        VoteRequestDto dto = new VoteRequestDto();
        dto.setCpf("123");
        dto.setVotingId(voting2.getId().toHexString());
        dto.setAnswer(Answer.NO);

        VoteResponseDto resp = votingService.addVote(dto);

        assertTrue(resp.isSuccess());
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowVotingExpired(){
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("test");
        Voting voting = new Voting(agenda, 1);

        voting.setId(id);
        voting.setExpirationDate(voting.getExpirationDate().minusSeconds(61));

        Mockito.when(votingRepository.findById(id)).thenReturn(java.util.Optional.of(voting));

        VoteRequestDto dto = new VoteRequestDto();
        dto.setCpf("123");
        dto.setVotingId(voting.getId().toHexString());
        dto.setAnswer(Answer.NO);
        votingService.addVote(dto);
    }

    @Test(expected = BusinessException.class)
    public void shouldNotReturnResultWhenVotingIsOpen(){
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("test");
        agenda.setId(id);

        Voting voting = new Voting(new Agenda("test"), 10);
        voting.addVote(new Vote("1", Answer.NO));

        Mockito.when(votingRepository.findById(id)).thenReturn(java.util.Optional.of(voting));

        votingService.getVotingResult(id.toHexString());
    }

    @Test
    public void shouldReturnVotingResult(){
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("test");
        agenda.setId(id);

        Voting voting = new Voting(new Agenda("test"), 1);
        voting.addVote(new Vote("1", Answer.NO));
        voting.addVote(new Vote("2", Answer.NO));
        voting.addVote(new Vote("3", Answer.YES));
        voting.setExpirationDate(Instant.now().minusSeconds(10));

        Mockito.when(votingRepository.findById(id)).thenReturn(java.util.Optional.of(voting));

        VotingResultResponseDto resp = votingService.getVotingResult(id.toHexString());

        assertEquals(2, resp.getVoteCount().getNo());
        assertEquals(1, resp.getVoteCount().getYes());
    }
}