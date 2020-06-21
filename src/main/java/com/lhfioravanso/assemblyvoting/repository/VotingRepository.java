package com.lhfioravanso.assemblyvoting.repository;

import com.lhfioravanso.assemblyvoting.entity.Voting;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingRepository extends MongoRepository<Voting, ObjectId> {
}
