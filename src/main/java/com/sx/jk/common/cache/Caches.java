package com.sx.jk.common.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;

public class Caches {
  private static final CacheManager MGR;
  private static final Cache<Object, Object> DEFAULT_CACHE;
  private static final Cache<Object, Object> TOKEN_CACHE;

  static {
    // 初始化缓存管理器
    URL url = Caches.class.getClassLoader().getResource("ehcache.xml");
    assert url != null;
    Configuration cfg = new XmlConfiguration(url);
    MGR = CacheManagerBuilder.newCacheManager(cfg);
    MGR.init();

    // 缓存对象
    DEFAULT_CACHE = MGR.getCache("default", Object.class, Object.class);
    TOKEN_CACHE = MGR.getCache("token", Object.class, Object.class);

    /* 如果使用磁盘或者堆外内存，需要使用序列化Serializable */
    // Caches<String, Serializable> defaultCache = MGR.getCache("default", String.class, Serializable.class);
    // Caches<String, Serializable> tokenCache = MGR.getCache("token", String.class, Serializable.class);
  }

  public static void put(Object key, Object value) {
    if (key == null || value == null) return;
    DEFAULT_CACHE.put(key, value);
  }

  public static void remove(Object key) {
    if (key == null) return;
    DEFAULT_CACHE.remove(key);
  }

  public static <T> T get(Object key) {
    if (key == null) return null;
    return (T) DEFAULT_CACHE.get(key);
  }

  public static void clear() {
    DEFAULT_CACHE.clear();
  }

  public static void putToken(Object key, Object value) {
    if (key == null || value == null) return;
    TOKEN_CACHE.put(key, value);
  }

  public static void removeToken(Object key) {
    if (key == null) return;
    TOKEN_CACHE.remove(key);
  }

  public static <T> T getToken(Object key) {
    if (key == null) return null;
    return (T) TOKEN_CACHE.get(key);
  }

  public static void clearToken() {
    TOKEN_CACHE.clear();
  }
}
