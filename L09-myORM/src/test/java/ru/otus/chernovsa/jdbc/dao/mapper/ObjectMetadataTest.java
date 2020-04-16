package ru.otus.chernovsa.jdbc.dao.mapper;

import org.junit.Test;
import ru.otus.chernovsa.core.dao.Id;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

class ClassWithPrimitiveFieldsAndIdAnnotation {
    @Id
    private int integ;
    private double dbl;
    private Double dDbl;
    private String str;

    public ClassWithPrimitiveFieldsAndIdAnnotation() {
    }
}

class ClassWithObjectFields {
    private ClassWithPrimitiveFieldsAndIdAnnotation i;
    private String s;

    public ClassWithObjectFields() {
    }
}

class ClassWithoutIdAnnotation {
    private int i;
    private double d;

    public ClassWithoutIdAnnotation() {
    }
}

class ClassWithSomeIdAnnotation {
    @Id
    private int i;
    @Id
    private double d;

    public ClassWithSomeIdAnnotation() {
    }
}


public class ObjectMetadataTest {
    ObjectMetadata<?> meta;

    public void initMeta() {
        try {
            meta = new ObjectMetadata<>(ClassWithPrimitiveFieldsAndIdAnnotation.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ObjectWithoutIdAnnotation() {
        Throwable thrown = catchThrowable(() -> new ObjectMetadata<>(ClassWithoutIdAnnotation.class));
        assertThat(thrown).isInstanceOf(ObjectMetadataException.class);
        assertThat(thrown.getMessage()).isEqualTo("Объект НЕ содержит поля с аннотацие Id.");
    }

    @Test
    public void ObjectWithObjectFields() {
        Throwable thrown = catchThrowable(() -> new ObjectMetadata<>(ClassWithObjectFields.class));
        assertThat(thrown).isInstanceOf(ObjectMetadataException.class);
        assertThat(thrown.getMessage()).isEqualTo("Объект состоит не только из примитивных типов.");
    }

    @Test
    public void ObjectWithSomeIdAnnotation() {
        Throwable thrown = catchThrowable(() -> new ObjectMetadata<>(ClassWithSomeIdAnnotation.class));
        assertThat(thrown).isInstanceOf(ObjectMetadataException.class);
        assertThat(thrown.getMessage()).isEqualTo("Объект содержит больше 1 поля с аннотацие Id.");
    }

    @Test
    public void getFields() {
        initMeta();
        List<Field> fields = meta.getFields();
        assertThat(fields.get(0).getName()).isEqualTo("integ");
        assertThat(fields.get(1).getName()).isEqualTo("dbl");
        assertThat(fields.get(2).getName()).isEqualTo("dDbl");
        assertThat(fields.get(3).getName()).isEqualTo("str");
    }

    @Test
    public void getFieldWithIdAnnotation() {
        initMeta();
        assertThat(meta.getFieldWithIdAnnotation().getName()).isEqualTo("integ");
    }

    @Test
    public void getFieldsForInsert() {
        initMeta();
        List<Field> fields = meta.getFieldsForInsert();
        assertThat(fields.get(0).getName()).isEqualTo("dbl");
        assertThat(fields.get(1).getName()).isEqualTo("dDbl");
        assertThat(fields.get(2).getName()).isEqualTo("str");
    }

    @Test
    public void getObjName() {
        initMeta();
        assertThat(meta.getClassName()).isEqualTo("ClassWithPrimitiveFieldsAndIdAnnotation");
    }
}