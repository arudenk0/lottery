package com.nexign.lottery.repos;

import com.nexign.lottery.entities.Winner;
import org.springframework.data.repository.CrudRepository;

public interface WinnerRepo extends CrudRepository<Winner, Long> {

}
