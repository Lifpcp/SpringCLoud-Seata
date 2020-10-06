package com.java.li.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lifuping
 * @PackageName:com.java.li.common.util
 * @date 2020-09-28 10:50:13
 * @description: 用于保存事务状态标识，防止防止幂等性问题导致的重复执行commit或者rollback
 */
public class ResultHolder {

    private static Map<Class<?>, Map<String, String>> map = new ConcurrentHashMap<Class<?>, Map<String, String>>();

    public static void setResult(Class<?> actionClass, String xid, String v) {
        Map<String, String> results = map.get(actionClass);

        if (results == null) {
            synchronized (map) {
                if (results == null) {
                    results = new ConcurrentHashMap<>();
                    map.put(actionClass, results);
                }
            }
        }

        results.put(xid, v);
    }

    public static String getResult(Class<?> actionClass, String xid) {
        Map<String, String> results = map.get(actionClass);
        if (results != null) {
            return results.get(xid);
        }

        return null;
    }

    public static void removeResult(Class<?> actionClass, String xid) {
        Map<String, String> results = map.get(actionClass);
        if (results != null) {
            results.remove(xid);
        }
    }
}
