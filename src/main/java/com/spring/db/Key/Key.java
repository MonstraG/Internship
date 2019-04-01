package com.spring.db.Key;

import java.io.Serializable;

public class Key implements Serializable {
    private Long id;
    private String key;

    Key(){}

    public Key(String key) {
        if (key == null)
            throw new IllegalArgumentException();
        this.key = key;
    }

    public Key(Long id, String key) {
        if (key == null)
            throw new IllegalArgumentException();
        this.id = id;
        this.key = key;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public void setKey(String key) { this.key = key; }
    public String getKey() { return key; }
}
