package com.gmail.txyjssr.mindmap.test.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.util.Log;

public class ReflectUtils {	

	public static Runnable getPostRunnable(final Object o, final String methodStr, final Object[] params) {
		final Method method = getMethod(o, methodStr);
		if (method != null) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						method.setAccessible(true);
						method.invoke(o, params);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			return r;
		}
		return null;
	}
	
	public static Object getField(final Object o, final String fieldName) {
		Class clazz = o.getClass();
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
			if(field!=null){
				field.setAccessible(true);
				return field.get(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}

	public static Method getMethod(Object o, String methodStr) {
		Method method = null;
		Class clazz = o.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		String[] tempStr = methodStr.split("\\(");
		String methodName = tempStr[0];
		for (Method m : methods) {
//			Log.i("yujsh log", m.getName());
			if (methodName.equals(m.getName())) {
				Class[] classes = m.getParameterTypes();
				StringBuffer sb = new StringBuffer();
				for (Class c : classes) {
					sb.append(c.getSimpleName() + ",");
				}
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}
				String result = sb.toString().replaceAll(" ", "");
				String[] str = tempStr[1].split("\\)");
				String params = "";
				if (str.length > 0) {
					params = tempStr[1].split("\\)")[0].replaceAll(" ", "");
				}
				if (params.equals(result)) {
					method = m;
					break;
				}
			}
		}

		return method;
	}
}
