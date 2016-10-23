package com.example.thinhtran1601.karaoke;

import java.io.Serializable;

/**
 * Created by thinh on 27/09/2016.
 */
public class Song implements Serializable {
    private String code;
    private String nameOfSong;
    private String singer;
    private boolean isLiked;

    public Song(String code, String nameOfSong, String singer) {
        this.code = code;
        this.nameOfSong = nameOfSong;
        this.singer = singer;
        this.isLiked = false;
    }

    public Song(String code, String nameOfSong, String singer, boolean isLiked) {
        this.code = code;
        this.nameOfSong = nameOfSong;
        this.singer = singer;
        this.isLiked = isLiked;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public String getCode() {
        return code;
    }

    public String getNameOfSong() {
        return nameOfSong;
    }

    public String getSinger() {
        return singer;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
