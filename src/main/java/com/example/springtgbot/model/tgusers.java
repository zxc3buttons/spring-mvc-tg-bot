package com.example.springtgbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table
@Entity
@SequenceGenerator(name="seq", sequenceName = "RTDS_ADSINPUT_SEQ", initialValue=1, allocationSize=1)
public class tgusers {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private int money;

    private Long chat_id;
}
