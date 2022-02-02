package com.example.springtgbot;

import lombok.Data;

@Data
public class Statistics {

    private String category;
    private Long moneyAmount;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Long moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public Statistics(String category, Long moneyAmount) {
        this.category = category;
        this.moneyAmount = moneyAmount;
    }

}
