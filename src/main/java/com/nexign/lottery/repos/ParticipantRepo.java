package com.nexign.lottery.repos;

import com.nexign.lottery.entities.Participant;
import org.springframework.data.repository.CrudRepository;

public interface ParticipantRepo extends CrudRepository<Participant, Long> {

}
