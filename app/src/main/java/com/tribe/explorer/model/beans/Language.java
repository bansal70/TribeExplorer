package com.tribe.explorer.model.beans;

/*
 * Created by rishav on 9/14/2017.
 */

public class Language {
    private String id, language, owner;

    public Language(String id, String language, String owner) {
        this.id = id;
        this.language = language;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getOwner() {
        return owner;
    }
}
