package ru.otus.chernovsa.myjsonwriter;

import java.lang.reflect.Field;
import java.util.*;

public class MyJsonWriter {
    private List<Triple> parsed = new ArrayList<>();

    class Triple {
        String name;
        FieldType type;
        Object value;

        public Triple(String name, FieldType type, Object value) {
            this.name = name;
            this.type = type;
            this.value = value;
        }
    }

    enum FieldType {OBJECT, STRING, NUMBER, BOOLEAN, NULL, COLLECTION, ARRAY}

    public void parse(Object object) throws IllegalAccessException {
        if (object == null) {
            parsed.add(new Triple("", FieldType.NULL, null));
        } else {
            parsed.addAll(parseInternal(object));
        }
    }

    private List<Triple> parseInternal(Object object) throws IllegalAccessException {
        List<Triple> objects = new ArrayList<>();
        if (object == null) {
            objects.add(new Triple("", FieldType.NULL, null));
        } else if (FieldType.OBJECT.equals(getFieldType(object))) {
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object val = field.get(object);
                FieldType fieldType = getFieldType(val);
                objects.add(new Triple(field.getName(), fieldType, val));
            }
        } else {
            FieldType fieldType = getFieldType(object);
            objects.add(new Triple(fieldType.name(), fieldType, object));
        }
        return objects;
    }

    public String toJSON() throws IllegalAccessException {
        StringBuilder result = new StringBuilder();
        result.append("{");
        if (parsed == null || parsed.size() == 0) {
            return result.append("}").toString();
        }

        for (int i = 0; i < parsed.size(); i++) {
            result.append(fieldToJson(parsed.get(i)));
            if (i < parsed.size() - 1) {
                result.append(", ");
            }
        }

        result.append("}");
        return result.toString();
    }

    private boolean arrayIsNotEmpty(Object[] objects) {
        for (Object object : objects) {
            if (object != null) {
                return true;
            }
        }
        return false;
    }

    private FieldType getFieldType(Object val) {
        if (val == null) {
            return FieldType.NULL;
        }
        FieldType result = FieldType.OBJECT;
        Class<?> fieldType = val.getClass();
        if (String.class.isAssignableFrom(fieldType)
                || Character.class.isAssignableFrom(fieldType) || char.class.isAssignableFrom(fieldType)) {
            result = FieldType.STRING;
        } else if (Boolean.class.isAssignableFrom(fieldType) || boolean.class.isAssignableFrom(fieldType)) {
            result = FieldType.BOOLEAN;
        } else if (Number.class.isAssignableFrom(fieldType) || fieldType.isPrimitive()) {
            result = FieldType.NUMBER;
        } else if (Collection.class.isAssignableFrom(fieldType)) {
            result = FieldType.COLLECTION;
        } else if (fieldType.isArray()) {
            result = FieldType.ARRAY;
        }
        return result;
    }

    private String fieldToJson(Triple field) throws IllegalAccessException {
        StringBuilder fldJson = new StringBuilder();
        fldJson.append("\"").append(field.name).append("\":");
        if (field.value == null) {
            return fldJson.append("null").toString();
        }

        if (FieldType.OBJECT.equals(field.type)) {
            return fldJson.append(ObjectToString(field.value)).toString();
        }

        if (FieldType.STRING.equals(field.type)) {
            return fldJson.append(StringToJson(field.value)).toString();
        }

        if (FieldType.NUMBER.equals(field.type)) {
            return fldJson.append(NumberToJson(field.value)).toString();
        }

        if (FieldType.BOOLEAN.equals(field.type)) {
            return fldJson.append(BooleanToJson(field.value)).toString();
        }

        if (FieldType.ARRAY.equals(field.type)) {
            return fldJson.append(ArrayToJson(field.value)).toString();
        }

        if (FieldType.COLLECTION.equals(field.type)) {
            return fldJson.append(CollectToJson(field.value)).toString();
        }

        return fldJson.toString();
    }

    private String StringToJson(Object obj) {
        return "\"" + obj + "\"";
    }

    private String NumberToJson(Object obj) {
        return String.format("%.2f", Double.valueOf(String.valueOf(obj))).replace(',', '.');
    }

    private String BooleanToJson(Object obj) {
        return String.valueOf(obj);
    }

    private String ArrayToJson(Object obj) throws IllegalAccessException {
        StringBuilder result = new StringBuilder();
        result.append("[");
        Object[] values;
        if (obj instanceof Object[]) {
            values = (Object[]) obj;
        } else {
            double[] arrPrimitive = (double[]) obj;
            values = new Object[arrPrimitive.length];
            for (int i = 0; i < arrPrimitive.length; i++) {
                values[i] = arrPrimitive[i];
            }
        }
        if (arrayIsNotEmpty(values)) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] == null) {
                    result.append("null");
                } else if (!FieldType.OBJECT.equals(getFieldType(values[i]))) {
                    List<Triple> flds = parseInternal(values[i]);
                    for (int j = 0; j < flds.size(); j++) {
                        String s = fieldToJson(flds.get(j));
                        result.append(s.substring(s.lastIndexOf(':') + 1));
                        if (j < flds.size() - 1) {
                            result.append(",");
                        }
                    }
                } else {
                    result.append(ObjectToString(values[i]));
                }
                if (i < values.length - 1) {
                    result.append(",");
                }
            }
        } else {
            result.append("null");
        }
        result.append("]");
        return result.toString();
    }

    private String CollectToJson(Object obj) throws IllegalAccessException {
        List<Object> values = (List<Object>) obj;
        int size = values.size();
        Object[] objects = values.toArray();
        return ArrayToJson(objects);
    }

    private String ObjectToString(Object obj) throws IllegalAccessException {
        StringBuilder result = new StringBuilder();
        result.append("{");
        List<Triple> flds = parseInternal(obj);
        for (int i = 0; i < flds.size(); i++) {
            result.append(fieldToJson(flds.get(i)));
            if (i < flds.size() - 1) {
                result.append(",");
            }
        }
        return result.append("}").toString();
    }

}
