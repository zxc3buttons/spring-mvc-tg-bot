package com.example.springtgbot.utils;

import com.vdurmont.emoji.EmojiParser;



public enum Emojis {
    RELAXED(EmojiParser.parseToUnicode(":relaxed:")),
    EXPENSES("\uD83D\uDCB8"),
    INCOMES("\uD83D\uDCB9"),
    GET_STATISTICS(EmojiParser.parseToUnicode(":eyes:")),
    ADD_EXPENSES(EmojiParser.parseToUnicode(":smiley_cat:")),
    ADD_INCOMES(EmojiParser.parseToUnicode(":crying_cat_face:")),
    DAILY_WALLET("\uD83D\uDC5B"),
    ACCUMULATIVE_WALLET("\uD83D\uDCB0");

    private final String emojiName;

    Emojis(String emojiName) {
        this.emojiName = emojiName;
    }

    @Override
    public String toString() {
        return emojiName;
    }
}
