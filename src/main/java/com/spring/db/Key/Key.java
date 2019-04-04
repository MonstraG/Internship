package com.spring.db.Key;

import java.io.Serializable;

public class Key implements Serializable {
    private String username;
    private String key;

    Key(){}

    public Key(String username, String key) {
        if (username == null || key == null)
            throw new IllegalArgumentException();
        this.username = username;
        this.key = key;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
}
