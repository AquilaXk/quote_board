package com.back;

public class Quote {
    int id;
    String content;
    String author;
    public Quote(int idx, String quote, String author) {
        this.id = idx;
        this.content = quote;
        this.author = author;
    }
}
