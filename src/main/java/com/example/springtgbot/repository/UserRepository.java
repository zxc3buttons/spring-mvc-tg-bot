package com.example.springtgbot.repository;

import com.example.springtgbot.Statistics;
import com.example.springtgbot.model.wallet_changes;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<wallet_changes, Integer> {

    @Query(value = "SELECT SUM(money) FROM wallet_changes WHERE chatId = ?1")
    int getSum(@Param("chat_id") long chatId);

    @Query(value = "SELECT SUM(money) FROM wallet_changes WHERE chatId = ?1 AND changeType = ?2 AND walletType = ?3 " +
            "AND date = ?4")
    int getChanges(@Param("chat_id") long chatId,
                   @Param("change_type") String changeType,
                   @Param("wallet_type")String walletType,
                   @Param("date")Date date);

    @Query(value = "SELECT SUM(money) FROM wallet_changes WHERE chatId = ?1 AND changeType = ?2 AND walletType = ?3 " +
            "AND date between ?4 and ?5")
    int getChangesByWeek(@Param("chat_id") long chatId,
                   @Param("change_type") String changeType,
                   @Param("wallet_type")String walletType,
                   Date date1,
                   Date date2);

    @Query(value = "SELECT new com.example.springtgbot.Statistics(w.category, SUM(w.money)) FROM wallet_changes w" +
            " WHERE date = ?2 " +
            "AND walletType = ?3 AND category IS NOT NULL AND changeType = ?4 AND chatId = ?1 " +
            "GROUP BY category ORDER BY SUM(money) DESC")
    List<Statistics> getChangesByCategoryForToday(@Param("chat_id") long chatId, Date date,
                                          @Param("wallet_type") String walletType, @Param("change_type")String changeType);

    @Query(value = "SELECT new com.example.springtgbot.Statistics(w.category, SUM(w.money)) FROM wallet_changes w" +
            " WHERE (date between ?2 and ?3) " +
            "AND walletType = ?3 AND category IS NOT NULL AND changeType = ?4 AND chatId = ?1 " +
            "GROUP BY category ORDER BY SUM(money) DESC")
    List<Statistics> getChangesByCategoryForDateInterval(@Param("chat_id") long chatId, Date date1, Date date2,
                                                  @Param("wallet_type") String walletType, @Param("change_type")String changeType);

    boolean existsByChatId(long chatId);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Modifying
    @Query(value = "UPDATE wallet_changes set category = ?1 where id IN(SELECT MAX(id) FROM wallet_changes)")
    void setCategoryForLastInsert(@Param("category")String category);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Modifying
    @Query(value = "UPDATE wallet_changes set money = ?1, chatId =?2, date=?3, changeType=?4" +
            " where id IN(SELECT MAX(id) FROM wallet_changes)")
    void setValuesForLastInsert(@Param("money")int money, @Param("chat_id") Long chatId,
                                    @Param("date") Date date, @Param("change_type") String changeType);
}
