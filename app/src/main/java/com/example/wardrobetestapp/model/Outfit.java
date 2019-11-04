package com.example.wardrobetestapp.model;

/**
 * Created by Nadimuddin on 4/11/19.
 */
public class Outfit {
    private int id;
    private String path;
    private boolean isTop;

    public Outfit id(int id) {
        this.id = id;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Outfit path(String path) {
        this.path = path;
        return this;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Outfit isTop(boolean isTop) {
        this.isTop = isTop;
        return this;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}
