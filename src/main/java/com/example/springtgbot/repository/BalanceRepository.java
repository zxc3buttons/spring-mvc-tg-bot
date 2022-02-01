package com.example.springtgbot.repository;

import com.example.springtgbot.model.balance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface BalanceRepository extends CrudRepository<balance, Integer> {
    boolean existsByChatIdAndWalletType(long chatId, String walletType);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Modifying
    @Query(value = "UPDATE balance set moneyAmount = ?2 where chatId = ?1 and walletType = ?3")
    void setNewBalance(@Param("chat_id")Long chatId, @Param("money_amount")Long moneyAmount,
                       @Param("wallet_type")String walletType);
}
