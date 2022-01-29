package com.example.springtgbot.repository;

import com.example.springtgbot.model.tgusers;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<tgusers, Integer> {

    @Query(value = "SELECT SUM(money) FROM tgusers WHERE chatId = ?1")
    int getSum(@Param("chat_id") long chatId);
}
