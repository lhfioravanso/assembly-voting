package com.lhfioravanso.assemblyvoting.api;

import com.lhfioravanso.assemblyvoting.dto.VoteRequestDto;
import com.lhfioravanso.assemblyvoting.dto.VoteResponseDto;
import com.lhfioravanso.assemblyvoting.dto.VotingRequestDto;
import com.lhfioravanso.assemblyvoting.dto.VotingResponseDto;
import com.lhfioravanso.assemblyvoting.service.VotingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@Api(value = "voting")
@RequestMapping(value = "api/v1/voting", produces = "application/json")
public class VotingController {

    private final VotingService votingService;

    @Autowired
    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @ApiOperation(value="get all votings", response = VotingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Votings found.")
    })
    @GetMapping()
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(this.votingService.listVotings());
    }

    @ApiOperation(value="get one voting", response = VotingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Voting found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id){
        return ResponseEntity.ok(this.votingService.getVoting(id));
    }

    @ApiOperation(value="create one voting", response = VotingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Voting successfully created.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody VotingRequestDto voting) throws URISyntaxException {
        VotingResponseDto response = this.votingService.createVoting(voting);
        return ResponseEntity.created(new URI(response.getId())).body(response);
    }

    @ApiOperation(value="add a vote in a voting", response = VotingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Vote successfully added.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/vote")
    public ResponseEntity<?> vote(@RequestBody VoteRequestDto vote) throws URISyntaxException {
        VoteResponseDto response = this.votingService.addVote(vote);
        return ResponseEntity.created(new URI(response.toString())).body(response);
    }

    @ApiOperation(value="get the voting result", response = VotingResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Voting result found.")
    })
    @GetMapping("/result/{id}")
    public ResponseEntity<?> getVotingResult(@PathVariable String id){
        return ResponseEntity.ok(this.votingService.getVotingResult(id));
    }
}
