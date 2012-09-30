package itcources.cache.impl;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Nikita Konovalov
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int SIZE;

    public LRUCache(int SIZE) {
        super();
        this.SIZE = SIZE;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if (SIZE ==  -1) {
            return false;
        }
        return size() > SIZE;
    }

}
