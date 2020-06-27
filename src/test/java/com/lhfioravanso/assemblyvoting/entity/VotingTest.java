package com.lhfioravanso.assemblyvoting.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class VotingTest {

    @Test
    public void shouldReturnTrueIfExpired() {
        Voting voting = new Voting();
        voting.setExpirationDate(Instant.now().minusSeconds(30));

        assertTrue(voting.isExpired());
    }

    @Test
    public void shouldReturnFalseIfNotExpired() {
        Voting voting = new Voting();
        voting.setExpirationDate(Instant.now().plusSeconds(30));

        assertFalse(voting.isExpired());
    }

    @Test
    public void shouldValidateCpfAlreadyVoted(){
        Voting voting = new Voting();

        voting.addVote(new Vote("111", Answer.NO));
        voting.addVote(new Vote("222", Answer.YES));
        voting.addVote(new Vote("111", Answer.YES));

        assertTrue(voting.cpfAlreadyVoted("111"));
    }

}