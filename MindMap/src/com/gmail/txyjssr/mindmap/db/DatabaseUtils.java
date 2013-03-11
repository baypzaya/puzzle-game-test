package com.gmail.txyjssr.mindmap.db;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import android.content.ContentValues;

public class DatabaseUtils {

	public static ContentValues transformObject(Object o) {
		ContentValues values = new ContentValues();
		try {
			Class c = o.getClass();
			Field[] fields = c.getFields();
			for (Field field : fields) {
				String key = field.getName();
				String fieldType = field.getType().getName();
				field.setAccessible(true);

				if (isStringType(fieldType)) {
					values.put(key, (String) field.get(o));
				}

				// int
				if (isIntegerType(fieldType)) {
					values.put(key, (Integer) field.get(o));
				}

				// boolean
				if (isBooleanType(fieldType)) {
					values.put(key, (Boolean) field.get(o));
				}

				// double
				if (isDoubleType(fieldType)) {
					values.put(key, (Double) field.get(o));
				}

				// byte
				if (isByteType(fieldType)) {
					values.put(key, (Byte) field.get(o));
				}

				// float
				if (isFloatType(fieldType)) {
					values.put(key, (Float) field.get(o));
				}
			}
		} catch (Exception e) {
		}
		return values;
	}

	public static Class getGenericType(Object o, int index) {
		Type genType = o.getClass().getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new RuntimeException("Index outof bounds");
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}

	public static Object getFieldVaule(Object obj, Field field) {
		String fieldType = field.getType().getName();
		field.setAccessible(true);

		Object objectFieldVaule = null;

		try {
			// String
			if (isStringType(fieldType)) {
				objectFieldVaule = (String) field.get(obj);
			}

			// int
			if (isIntegerType(fieldType)) {
				objectFieldVaule = (Integer) field.get(obj);
			}

			// boolean
			if (isBooleanType(fieldType)) {
				objectFieldVaule = (Boolean) field.get(obj);
			}

			// double
			if (isDoubleType(fieldType)) {
				objectFieldVaule = (Double) field.get(obj);
			}

			// byte
			if (isByteType(fieldType)) {
				objectFieldVaule = (Byte) field.get(obj);
			}

			// float
			if (isFloatType(fieldType)) {
				objectFieldVaule = (Float) field.get(obj);
			}
		} catch (Exception e) {

		}
		return objectFieldVaule;
	}

	public static String getColumnType(Field field) {
		String fieldType = field.getType().getName();
		String columType = null;

		try {
			// String
			if (isStringType(fieldType)) {
				columType = "VARCHAR";
			}

			// int
			if (isIntegerType(fieldType)) {
				columType = "INTEGER";
			}

			// boolean
			if (isBooleanType(fieldType)) {
				columType = "BOOL";
			}

			// double
			if (isDoubleType(fieldType)) {
				columType = "DOUBLE";
			}

			// byte
			if (isByteType(fieldType)) {
				columType = "TEXT";
			}

			// float
			if (isFloatType(fieldType)) {
				columType = "FLOAT";
			}
		} catch (Exception e) {

		}
		return columType;
	}

	private static boolean isFloatType(String fieldType) {
		return fieldType.equals("float") || fieldType.equals("java.lang.Float");
	}

	private static boolean isByteType(String fieldType) {
		return fieldType.equals("byte") || fieldType.equals("java.lang.Byte");
	}

	private static boolean isDoubleType(String fieldType) {
		return fieldType.equals("double") || fieldType.equals("java.lang.Double");
	}

	private static boolean isBooleanType(String fieldType) {
		return fieldType.equals("boolean") || fieldType.equals("java.lang.Boolean");
	}

	private static boolean isIntegerType(String fieldType) {
		return fieldType.equals("int") || fieldType.equals("java.lang.Integer");
	}

	private static boolean isStringType(String fieldType) {
		return fieldType.equals("java.lang.String");
	}
}
