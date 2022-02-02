package com.example.springtgbot.utils;

import com.vdurmont.emoji.EmojiParser;



public enum Emojis {
    RELAXED(EmojiParser.parseToUnicode(":relaxed:")),
    EXPENSES(EmojiParser.parseToUnicode(":confused:")),
    INCOMES(EmojiParser.parseToUnicode(":grin:")),
    GET_STATISTICS(EmojiParser.parseToUnicode(":eyes:")),
    ADD_EXPENSES(EmojiParser.parseToUnicode(":smiley_cat:")),
    ADD_INCOMES(EmojiParser.parseToUnicode(":crying_cat_face:"));

    private final String emojiName;

    Emojis(String emojiName) {
        this.emojiName = emojiName;
    }

    @Override
    public String toString() {
        return emojiName;
    }
}
