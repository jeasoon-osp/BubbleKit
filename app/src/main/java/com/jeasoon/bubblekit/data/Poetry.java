package com.jeasoon.bubblekit.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Poetry {

//    {
//        "author": "劉得仁",
//        "paragraphs": [
//          "朱檻滿明月，美人歌落梅。",
//          "忽驚塵起處，疑是有風來。",
//          "一曲聽初徹，幾年愁暫開。",
//          "東南正雲雨，不得見陽臺。"
//        ],
//        "title": "聽歌",
//        "id": "9a280978-cd68-4d49-bf3a-d197d5b0bd63"
//    }

    @SerializedName("author")
    private String author;

    @SerializedName("paragraphs")
    private List<String> paragraphs;

    @SerializedName("title")
    private String title;

    @SerializedName("id")
    private String id;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toParagraphsString() {
        List<String> lines = getParagraphs();
        StringBuilder sb = new StringBuilder();
        sb.append(getTitle())
                .append("\n")
                .append(getAuthor())
                .append("\n");
        boolean isFirst = true;
        for (String line : lines) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("\n");
            }
            sb.append(line);
        }
        return sb.toString();
    }
}
