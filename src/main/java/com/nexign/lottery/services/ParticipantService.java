package com.nexign.lottery.services;

import com.nexign.lottery.dtos.ParticipantDto;
import com.nexign.lottery.entities.Participant;
import com.nexign.lottery.repos.ParticipantRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepo repo;

    public ParticipantService(ParticipantRepo repo) {
        this.repo = repo;
    }

    /**
     * Add new participant
     * @param dto participant dto
     * @return Participant that was just added in database
     */
    public Participant addParticipant(ParticipantDto dto) {
        return repo.save(new Participant(dto));
    }

    /**
     * Get all participants
     * @return all participants from database
     */
    public Iterable<Participant> getAll() {
        return repo.findAll();
    }

    /**
     * Try to find participant by id
     * @param id paricipant id
     * @return participant if success
     */
    public Optional<Participant> getById(long id) {
        return repo.findById(id);
    }

    /**
     * Delete all records
     */
    public void CleanDb() {
        repo.deleteAll();
    }
}
