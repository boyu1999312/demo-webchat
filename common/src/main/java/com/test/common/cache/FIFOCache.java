package com.test.common.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class FIFOCache {
    private final int MAX_CACHE_SIZE;
    private final float DEFAULT_LOAD_FACTORY = 0.75f;

    LinkedHashMap<String, String> map;


    public FIFOCache(int cacheSize){
        MAX_CACHE_SIZE = cacheSize;
        int capacity = (int) (Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTORY) +1);

        map = new LinkedHashMap<String, String>(capacity, MAX_CACHE_SIZE, false){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
    }

    public void put(String k, String v){
        map.put(k, v);
    }

    public String get(String k){
        return map.get(k);
    }


}
