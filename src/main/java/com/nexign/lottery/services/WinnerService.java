package com.nexign.lottery.services;


import com.nexign.lottery.entities.Winner;
import com.nexign.lottery.repos.WinnerRepo;
import org.springframework.stereotype.Service;


@Service
public class WinnerService {

    private final WinnerRepo repo;

    public WinnerService(WinnerRepo repo) {
        this.repo = repo;
    }

    /**
     * Add new winner
     * @param winner
     * @return winner that was just added in database
     */
    public Winner addWinner(Winner winner) {
        return repo.save(winner);
    }

    /**
     * Get all winners
     * @return all winners from database
     */
    public Iterable<Winner> getAll() {
        return repo.findAll();
    }

}
