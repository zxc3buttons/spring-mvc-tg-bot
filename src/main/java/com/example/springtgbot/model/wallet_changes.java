package com.example.springtgbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Table
@Entity
@SequenceGenerator(name="seq", sequenceName = "RTDS_ADSINPUT_SEQ", initialValue=1, allocationSize=1)
public class wallet_changes {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;
    private int money;
    private Date date;
    private String category;
    @Column(name = "change_type")
    private String changeType;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "wallet_type")
    private String walletType;
}
