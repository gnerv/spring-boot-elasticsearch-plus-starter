package com.gnerv.plus.elasticsearch.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ligen
 */
public class ContextCache implements Serializable {
 
  private static final long serialVersionUID = 1L;
 
  public static final ThreadLocal<Map<String,String>> CACHE = new ThreadLocal<>();
 
  /**
   * 放数据
   * @param sourceKey
   */
  public static final void putAttribute(String sourceKey,String value){
    Map<String,String> cacheMap = CACHE.get();
    if(null == cacheMap){
      cacheMap = new HashMap<>();
    }
    cacheMap.put(sourceKey,value);
    CACHE.set(cacheMap);
  }
 
  /**
   * 拿数据
   * @param sourceKey
   */
  public static final String getAttribute(String sourceKey){
    Map<String,String> cacheMap = CACHE.get();
    if(null == cacheMap){
      return null;
    }
    return cacheMap.get(sourceKey);
  }
 
}