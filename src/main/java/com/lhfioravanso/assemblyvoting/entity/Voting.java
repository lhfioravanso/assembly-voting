package com.lhfioravanso.assemblyvoting.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "voting")
public class Voting {

    @Id
    private ObjectId id;

    private Agenda agenda;

    private Integer minutesToExpiration;

    private Instant expirationDate;

    private List<Vote> votes;

    public Voting(Agenda agenda, Integer minutesToExpiration) {
        this.agenda = agenda;
        this.minutesToExpiration = minutesToExpiration;
        this.expirationDate = Instant.now().plusSeconds(minutesToExpiration * 60);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Integer getMinutesToExpiration() {
        return minutesToExpiration;
    }

    public void setMinutesToExpiration(Integer minutesToExpiration) {
        this.minutesToExpiration = minutesToExpiration;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
