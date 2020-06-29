package com.lhfioravanso.assemblyvoting.service;

import com.lhfioravanso.assemblyvoting.dto.VotingResultResponseDto;
import com.lhfioravanso.assemblyvoting.kafka.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingServiceImpl implements MessagingService {

    private final Producer producer;

    @Autowired
    public MessagingServiceImpl(Producer producer) {
        this.producer = producer;
    }

    @Override
    public void send(VotingResultResponseDto votingResult) {
        producer.send(String.format("Agenda '%s' closed! Votes: [Yes= %d] ~ [No= %d]",
                votingResult.getAgenda().getName(),
                votingResult.getVoteCount().getYes(),
                votingResult.getVoteCount().getNo()
        ));
    }
}
