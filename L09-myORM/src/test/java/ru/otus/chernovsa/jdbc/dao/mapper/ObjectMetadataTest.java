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

    public ClassWithPrimitiveFieldsAndIdAnnotation(int i, double d, Double dd, String s) {
        this.integ = i;
        this.dbl = d;
        this.dDbl = dd;
        this.str = s;
    }
}

class ClassWithObjectFields {
    private ClassWithPrimitiveFieldsAndIdAnnotation i;
    private String s;

    public ClassWithObjectFields(ClassWithPrimitiveFieldsAndIdAnnotation i, String s) {
        this.i = i;
        this.s = s;
    }
}

class ClassWithoutIdAnnotation {
    private int i;
    private double d;

    public ClassWithoutIdAnnotation(int i, double d) {
        this.i = i;
        this.d = d;
    }
}

class ClassWithSomeIdAnnotation {
    @Id
    private int i;
    @Id
    private double d;

    public ClassWithSomeIdAnnotation(int i, double d) {
        this.i = i;
        this.d = d;
    }
}


public class ObjectMetadataTest {
    ObjectMetadata meta;

    public void initMeta() {
        try {
            meta = new ObjectMetadata(new ClassWithPrimitiveFieldsAndIdAnnotation(0, 1.0, 2.0, "test"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ObjectWithoutIdAnnotation() {
        Throwable thrown = catchThrowable(() -> {
            new ObjectMetadata(new ClassWithoutIdAnnotation(1, 1.0));
        });
        assertThat(thrown).isInstanceOf(ObjectMetadataException.class);
        assertThat(thrown.getMessage()).isEqualTo("Объект НЕ содержит поля с аннотацие Id.");
    }

    @Test
    public void ObjectWithObjectFields() {
        Throwable thrown = catchThrowable(() -> {
            new ObjectMetadata(new ClassWithObjectFields(
                    new ClassWithPrimitiveFieldsAndIdAnnotation(1, 1.0, 1.0, "s"), "test")
            );
        });
        assertThat(thrown).isInstanceOf(ObjectMetadataException.class);
        assertThat(thrown.getMessage()).isEqualTo("Объект состоит не только из примитивных типов.");
    }

    @Test
    public void ObjectWithSomeIdAnnotation() {
        Throwable thrown = catchThrowable(() -> {
            new ObjectMetadata(new ClassWithSomeIdAnnotation(1, 1.0));
        });
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
        assertThat(meta.getObjName()).isEqualTo("ClassWithPrimitiveFieldsAndIdAnnotation");
    }
}