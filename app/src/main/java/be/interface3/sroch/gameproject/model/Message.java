package be.interface3.sroch.gameproject.model;

import java.util.Date;

/**
 * Created by s.roch on 30/09/2016.
 */
public class Message extends DatabaseElement {
    long id;
    User author;
    Character alias;
    String type;
    Date postDate;
    String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Character getAlias() {
        return alias;
    }

    public void setAlias(Character alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
