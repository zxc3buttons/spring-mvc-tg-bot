package com.example.springtgbot.repository;

import com.example.springtgbot.model.tgusers;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<tgusers, Integer> {

    @Query(value = "SELECT SUM(money) FROM tgusers WHERE chatId = ?1")
    int getSum(@Param("chat_id") long chatId);

    @Query(value = "SELECT SUM(money) FROM tgusers WHERE chatId = ?1 AND changeType = ?2")
    int getExpenses(@Param("chat_id") long chatId, @Param("change_type") String changeType);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Modifying
    @Query(value = "UPDATE tgusers set category = ?1 where id IN(SELECT MAX(id) FROM tgusers)")
    void setCategoryForLastInsert(@Param("category")String category);
}
