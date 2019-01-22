package com.bbz.test.model;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;


public class Article implements Comparable<Article>{
    private final Logger logger = LoggerFactory.getLogger(Article.class);

    private int id;

    private String author;

    private String title;

    private String content;

    private long creationTimestamp;

    private byte[] svgIdenticon;

    public Article() {
    }

    public Article(String author, String title, String content) {
        this.author = author;
        this.title = title.trim();
        this.content = content;
        this.creationTimestamp = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getCreationTimestampFormatted(String format) {
        try {
            Date date = new Date(this.creationTimestamp);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            logger.debug("Failed formatting timestamp: " + e.getMessage());
            return "";
        }

    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public byte[] getSvgIdenticonByteArr() {
        return svgIdenticon;
    }

    public String getSvgIdenticonBase64Str() {
        return Base64.encode(svgIdenticon);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Article(int id, String author, String title, String content, long creationTimestamp, byte[] svgIdenticon) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.creationTimestamp = creationTimestamp;
        this.svgIdenticon = svgIdenticon;
    }


    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                ", svgIdenticon=" + Arrays.toString(svgIdenticon) +
                '}';
    }

    @Override
    public int compareTo(Article that) {
        return Long.compare(this.creationTimestamp, that.creationTimestamp);
    }
}
