package com.xyh.dubbo.threadlocal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hcxyh  2018年8月16日
 *
 */
public class ReqContext {
	
	private static final ThreadLocal<Map<String, Object>> REQUEST_CONTEXTS = new ThreadLocal<Map<String, Object>>() {
	    @Override
	    protected Map<String, Object> initialValue() {
	      return new HashMap<>();
	    }
	  };

	  private static Map<String, Object> findContext() {
	    return REQUEST_CONTEXTS.get();
	  }

	  public static Object get(String key) {
	    return findContext().get(key);
	  }

	  public static String getString(String key) {
	    Object v = get(key);
	    if (v == null) {
	      return null;
	    }

	    return v.toString();
	  }

	  public static Byte getByte(String key) {
	    Object v = get(key);
	    if (v == null) {
	      return null;
	    }

	    if (v instanceof Byte) {
	      return (Byte) v;
	    }
	    return null;
	  }

	  public static Character getChar(String key) {
	    Object v = get(key);
	    if (v == null) {
	      return null;
	    }

	    if (v instanceof Character) {
	      return (Character) v;
	    }
	    return null;
	  }

	  public static Short getShort(String key) {
	    Object v = get(key);
	    if (v == null) {
	      return null;
	    }

	    if (v instanceof Short) {
	      return (Short) v;
	    }

	    if (v instanceof Number) {
	      return Short.valueOf(((Number) v).shortValue());
	    }
	    return Short.parseShort(v.toString());
	  }

	  public static Integer getInteger(String key) {
	    Object v = get(key);
	    if (v == null) {
	      return null;
	    }

	    if (v instanceof Integer) {
	      return (Integer) v;
	    }

	    if (v instanceof Number) {
	      return Integer.valueOf(((Number) v).intValue());
	    }
	    return Integer.valueOf(v.toString());
	  }

	  public static Long getLong(String key) {
	    Object v = get(key);
	    if (v == null) {
	      return null;
	    }

	    if (v instanceof Long) {
	      return (Long) v;
	    }

	    if (v instanceof Number) {
	      return Long.valueOf(((Number) v).longValue());
	    }

	    return Long.valueOf(v.toString());
	  }

	  public static Float getFloat(String key) {
	    Object v = get(key);
	    if (v == null) {
	      return null;
	    }

	    if (v instanceof Float) {
	      return (Float) v;
	    }

	    if (v instanceof Number) {
	      return ((Number) v).floatValue();
	    }
	    return Float.parseFloat(v.toString());
	  }

	  public static Double getDouble(String key) {
	    Object v = get(key);
	    if (v == null) {
	      return null;
	    }

	    if (v instanceof Double) {
	      return (Double) v;
	    }

	    if (v instanceof Number) {
	      return ((Number) v).doubleValue();
	    }
	    return Double.valueOf(v.toString());
	  }

	  public static <T> T getValue(String key) {
	    return (T) get(key);
	  }

	  public static void put(String key, Object value) {
	    findContext().put(key, value);
	  }

	  public static void putAll(Map<String, Object> values) {
	    findContext().putAll(values);
	  }

	  public static Object remove(String key) {
	    return findContext().remove(key);
	  }

	  public static void clear() {
	    findContext().clear();
	  }
}
