package com.safframework.rxcache.memory.algorithm.lru;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.exception.RxCacheException;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by tony on 2018/10/21.
 */
public class LRUCache<K,V> {

    private Map<K,V> cache = null;
    private AbstractQueue<K> queue = null;
    private int size = 0;

    public LRUCache() {

        this(Constant.DEFAULT_CACHE_SIZE);
    }

    public LRUCache(int size) {

        this.size = size;
        cache = new ConcurrentHashMap<K,V>(size);
        queue = new ConcurrentLinkedQueue<K>();
    }

    public boolean containsKey(K key) {

        return cache.containsKey(key);
    }

    public V get(K key) {

        //Recently accessed, hence move it to the tail
        queue.remove(key);
        queue.add(key);
        return cache.get(key);
    }

    public V getSilent(K key) {

        return cache.get(key);
    }

    public void put(K key, V value) {

        //ConcurrentHashMap doesn't allow null key or values
        if(key == null || value == null) throw new RxCacheException("key is null or value is null");

        if(cache.containsKey(key)) {
            queue.remove(key);
        }

        if(queue.size() >= size) {
            K lruKey = queue.poll();
            if(lruKey != null) {
                cache.remove(lruKey);
            }
        }

        queue.add(key);
        cache.put(key,value);
    }

    /**
     * 获取最近最少使用的值
     * @return
     */
    public V getLeastRecentlyUsed() {

        K remove = queue.remove();
        queue.add(remove);
        return cache.get(remove);
    }

    public void remove(K key) {

        cache.remove(key);
        queue.remove(key);
    }

    public void clear() {

        cache.clear();
        queue.clear();
    }

    @Override
    public synchronized String toString() {

        Iterator<K> iterator = queue.iterator();
        StringBuilder sb = new StringBuilder();

        while (iterator.hasNext()) {
            K key = iterator.next();
            sb.append("{ ");
            sb.append(key);
            sb.append(":");
            sb.append(this.getSilent(key));
            sb.append(" }");
            if(iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
