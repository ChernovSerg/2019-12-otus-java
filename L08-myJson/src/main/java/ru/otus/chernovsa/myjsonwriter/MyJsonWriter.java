package ru.otus.chernovsa.myjsonwriter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class MyJsonWriter {

    private static class Triple {
        String name;
        FieldType type;
        Object value;

        public Triple(String name, FieldType type, Object value) {
            this.name = name;
            this.type = type;
            this.value = value;
        }
    }

    public String toJSON(Object object) throws IllegalAccessException {
        if (object == null) {
            return "null";
        }
        FieldType rootType = getFieldType(object);
        boolean rootIsObj = rootType.equals(FieldType.OBJECT);
        StringBuilder result = new StringBuilder();
        result.append(rootIsObj ? "{" : "");
        List<Triple> parsed = new ArrayList<>(parseInternal(object, rootType));
        for (int i = 0; i < parsed.size(); i++) {
            Triple triple = parsed.get(i);
            result.append(rootIsObj ? "\"" + triple.name + "\":" : "");
            result.append(fieldToJson(triple));
            if (i < parsed.size() - 1) {
                result.append(",");
            }
        }
        result.append(rootIsObj ? "}" : "");
        return result.toString();
    }

    private List<Triple> parseInternal(Object object, FieldType rootType) throws IllegalAccessException {
        List<Triple> objects = new ArrayList<>();
        if (object == null) {
            objects.add(new Triple("", FieldType.NULL, null));
        }
        if (FieldType.OBJECT.equals(rootType)) {
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object val = field.get(object);
                FieldType fldType = getFieldType(val);
                objects.add(new Triple(field.getName(), fldType, val));
            }
        } else {
            objects.add(new Triple(rootType.name(), rootType, object));
        }
        return objects;
    }

    private FieldType getFieldType(Object val) {
        if (val == null) {
            return FieldType.NULL;
        }
        FieldType result;
        Class<?> fieldType = val.getClass();
        if (String.class.isAssignableFrom(fieldType)
                || Character.class.isAssignableFrom(fieldType) || char.class.isAssignableFrom(fieldType)) {
            result = FieldType.STRING;
        } else if (Boolean.class.isAssignableFrom(fieldType) || boolean.class.isAssignableFrom(fieldType)) {
            result = FieldType.BOOLEAN;
        } else if (Byte.class.isAssignableFrom(fieldType) || Short.class.isAssignableFrom(fieldType)
                || Integer.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType)) {
            result = FieldType.INTEGER;
        } else if (Float.class.isAssignableFrom(fieldType) || Double.class.isAssignableFrom(fieldType)) {
            result = FieldType.DOUBLE;
        } else if(Queue.class.isAssignableFrom(fieldType)) {
            result = FieldType.QUEUE;
        } else if(Set.class.isAssignableFrom(fieldType)) {
            result = FieldType.SET;
        } else if (List.class.isAssignableFrom(fieldType)) {
            result = FieldType.LIST;
        } else if (fieldType.isArray()) {
            result = FieldType.ARRAY;
        } else {
            result = FieldType.OBJECT;
        }
        return result;
    }

    private String fieldToJson(Triple field) throws IllegalAccessException {
        StringBuilder fldJson = new StringBuilder();

        if (field.value == null) {
            return fldJson.append("null").toString();
        }

        if (FieldType.OBJECT.equals(field.type)) {
            return fldJson.append(objectToString(field.value)).toString();
        }

        if (FieldType.STRING.equals(field.type)) {
            return fldJson.append("\"").append(field.value).append("\"").toString();
        }

        if (FieldType.INTEGER.equals(field.type)
                || FieldType.DOUBLE.equals(field.type)
                || FieldType.BOOLEAN.equals(field.type)) {
            return fldJson.append(field.value).toString();
        }

        if (FieldType.ARRAY.equals(field.type)) {
            return fldJson.append(arrayToJson(field.value)).toString();
        }

        if (FieldType.LIST.equals(field.type)) {
            return fldJson.append(listToJson(field.value)).toString();
        }

        if (FieldType.QUEUE.equals(field.type)) {
            return fldJson.append(queueToJson(field.value)).toString();
        }

        if (FieldType.SET.equals(field.type)) {
            return fldJson.append(setToJson(field.value)).toString();
        }

        return fldJson.toString();
    }

    private String arrayToJson(Object objects) throws IllegalAccessException {
        StringBuilder result = new StringBuilder();
        result.append("[");
        int length = Array.getLength(objects);
        if (length == 0) {
            return result.append("]").toString();
        }
        for (int i = 0; i < length; i++) {
            result.append(toJSON(Array.get(objects, i)));
            if (i < length - 1) {
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }

    private String listToJson(Object obj) throws IllegalAccessException {
        List<Object> values = (List<Object>) obj;
        Object[] objects = values.toArray();
        return arrayToJson(objects);
    }

    private String queueToJson(Object obj) throws IllegalAccessException {
        Queue<Object> values = (Queue<Object>) obj;
        Object[] objects = values.toArray();
        return arrayToJson(objects);
    }

    private String setToJson(Object obj) throws IllegalAccessException {
        Set<Object> values = (Set<Object>) obj;
        Object[] objects = values.toArray();
        return arrayToJson(objects);
    }

    private String objectToString(Object obj) throws IllegalAccessException {
        return toJSON(obj);
    }

}
