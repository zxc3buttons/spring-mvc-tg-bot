package com.example.springtgbot.repository;

import com.example.springtgbot.model.tgusers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<tgusers, Integer> {

}
