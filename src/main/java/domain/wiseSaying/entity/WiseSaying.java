package domain.wiseSaying.entity;

public class WiseSaying {
    private int id;
    private String content;
    private String author;

    public WiseSaying(int idx, String quote, String author) {
        this.id = idx;
        this.content = quote;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}
