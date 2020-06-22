package com.lhfioravanso.assemblyvoting.api;

import com.lhfioravanso.assemblyvoting.dto.VoteRequestDto;
import com.lhfioravanso.assemblyvoting.dto.VotingRequestDto;
import com.lhfioravanso.assemblyvoting.dto.VotingResponseDto;
import com.lhfioravanso.assemblyvoting.service.VotingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "voting")
@RequestMapping(value = "v1/voting", produces = "application/json")
public class VotingController {

    private final VotingService votingService;

    @Autowired
    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @ApiOperation(value="get all votings", response = VotingResponseDto.class)
    @GetMapping()
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(this.votingService.listVotings());
    }

    @ApiOperation(value="get one voting", response = VotingResponseDto.class)
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id){
        return ResponseEntity.ok(this.votingService.getVoting(id));
    }

    @ApiOperation(value="create one voting", response = VotingResponseDto.class)
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody VotingRequestDto voting){
        return ResponseEntity.ok(this.votingService.createVoting(voting));
    }

    @ApiOperation(value="add a vote in a voting", response = VotingResponseDto.class)
    @PutMapping("/vote")
    public ResponseEntity<?> vote(@RequestBody VoteRequestDto vote){
        return ResponseEntity.ok(this.votingService.addVote(vote));
    }
}
