package com.lhfioravanso.assemblyvoting.repository;

import com.lhfioravanso.assemblyvoting.entity.Agenda;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, ObjectId> {
}
