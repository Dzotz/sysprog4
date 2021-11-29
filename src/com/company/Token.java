package com.company;

public class Token {
    LexemeType type;
    String word;
    public Token(LexemeType type, String word) {
        this.type = type;
        this.word = word;
    }

    public Token(LexemeType type, char word) {
        this.type = type;
        this.word = String.valueOf(word);
    }

    public Token(LexemeType type, Integer word) {
        this.type = type;
        this.word = String.valueOf(word);
    }

    public Token(LexemeType type, Double word) {
        this.type = type;
        this.word = String.valueOf(word);
    }

    public String toString() {
        return this.type.toString() + "\t" + this.word;
    }
    public String toRawString() { return word; }
}
