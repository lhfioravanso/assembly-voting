package com.lhfioravanso.assemblyvoting.api;

import com.lhfioravanso.assemblyvoting.dto.*;
import com.lhfioravanso.assemblyvoting.entity.VoteCount;
import com.lhfioravanso.assemblyvoting.service.VotingService;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class VotingControllerTest {

    @Mock
    public VotingService votingService;

    @InjectMocks
    public VotingController votingController;

    @Test
    public void shouldReturnZeroVotings() {
        ResponseEntity<?> resp = votingController.getAll();

        assertEquals(resp.getStatusCode(), HttpStatus.OK);
        assertEquals(0, ((LinkedList<?>) resp.getBody()).size());
    }

    @Test
    public void shouldReturnVotings() {
        List<VotingResponseDto> listDto = new ArrayList<>();
        VotingResponseDto voting = new VotingResponseDto();
        listDto.add(voting);

        Mockito.when(votingService.listVotings()).thenReturn(listDto);

        ResponseEntity<?> resp = votingController.getAll();

        assertEquals(resp.getStatusCode(), HttpStatus.OK);
        assertEquals(1, ((ArrayList<?>) resp.getBody()).size());
    }

    @Test
    public void shouldCreateVoting() throws URISyntaxException {
        VotingRequestDto req = new VotingRequestDto();
        VotingResponseDto resp = new VotingResponseDto();

        resp.setId(new ObjectId().toHexString());
        Mockito.when(votingService.createVoting(req)).thenReturn(resp);

        ResponseEntity<?> response = votingController.create(req);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldVote() throws URISyntaxException {
        VoteRequestDto req = new VoteRequestDto();
        VoteResponseDto resp = new VoteResponseDto(true);

        Mockito.when(votingService.addVote(req)).thenReturn(resp);

        ResponseEntity<?> response = votingController.vote(req);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldGetVotingResult(){
        String id = new ObjectId().toHexString();
        VotingResultResponseDto resp = new VotingResultResponseDto();
        resp.setAgenda(new AgendaResponseDto(id, "test"));
        resp.setVoteCount(new VoteCount(1L,2L));

        Mockito.when(votingService.getVotingResult(id)).thenReturn(resp);

        ResponseEntity<?> response = votingController.getVotingResult(id);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }
}