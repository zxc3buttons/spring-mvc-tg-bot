package com.example.springtgbot.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Table
@Entity
@SequenceGenerator(name="seq", sequenceName = "balance_seq", initialValue=1, allocationSize=1)
public class balance {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;
    @Column(name = "wallet_type")
    private String walletType;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "money_amount")
    private Long moneyAmount;

}
