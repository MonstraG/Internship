package com.spring.db.Key;

import java.util.List;

public interface KeyDAO {
    Key getKeyById(Long id);

    Key getKeyByValue(String key);

    List<Key> getAllKeys();

    boolean createKey(Key key);

    boolean updateKey(Key key);

    boolean deleteKey(Key key);

    boolean keyExists(String key);
}
