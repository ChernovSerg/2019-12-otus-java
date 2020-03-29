package ru.otus.chernovsa.myjsonwriter;

import com.google.gson.Gson;
import org.junit.Test;
import ru.otus.chernovsa.myjsonwriter.demo.Other2;
import ru.otus.chernovsa.myjsonwriter.demo.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class MyJsonWriterTest {
    MyJsonWriter myJson = new MyJsonWriter();
    Gson gson = new Gson();

    @Test
    public void testNull() throws IllegalAccessException {
        assertThat(myJson.toJSON(null)).isEqualTo(gson.toJson(null));
    }

    @Test
    public void testByte() throws IllegalAccessException {
        assertThat(myJson.toJSON((byte) 1)).isEqualTo(gson.toJson((byte) 1));
    }

    @Test
    public void testShort() throws IllegalAccessException {
        assertThat(myJson.toJSON((short) 2)).isEqualTo(gson.toJson((short) 2));
    }

    @Test
    public void testInt() throws IllegalAccessException {
        assertThat(myJson.toJSON(3)).isEqualTo(gson.toJson(3));
    }

    @Test
    public void testLong() throws IllegalAccessException {
        assertThat(myJson.toJSON(4L)).isEqualTo(gson.toJson(4L));
    }

    @Test
    public void testFloat() throws IllegalAccessException {
        assertThat(myJson.toJSON(5.045345f)).isEqualTo(gson.toJson(5.045345f));
    }

    @Test
    public void testDouble() throws IllegalAccessException {
        assertThat(myJson.toJSON(6.543212d)).isEqualTo(gson.toJson(6.543212d));
    }

    @Test
    public void testString() throws IllegalAccessException {
        assertThat(myJson.toJSON("a")).isEqualTo(gson.toJson("a"));
        assertThat(myJson.toJSON("aaa")).isEqualTo(gson.toJson("aaa"));
    }

    @Test
    public void testArrayString() throws IllegalAccessException {
        String[] strs = {"Hello, ", "Sergey!"};
        assertThat(myJson.toJSON(strs)).isEqualTo(gson.toJson(strs));
    }

    @Test
    public void testChar() throws IllegalAccessException {
        assertThat(myJson.toJSON('a')).isEqualTo(gson.toJson('a'));
    }

    @Test
    public void testArrayEmpty() throws IllegalAccessException {
        assertThat(myJson.toJSON(new int[]{})).isEqualTo(gson.toJson(new int[]{}));
    }

    @Test
    public void testArrayFull() throws IllegalAccessException {
        assertThat(myJson.toJSON(new int[]{7, 8, 9})).isEqualTo(gson.toJson(new int[]{7, 8, 9}));
        assertThat(myJson.toJSON(List.of(10, 11, 12))).isEqualTo(gson.toJson(List.of(10, 11, 12)));
    }

    @Test
    public void testCollectionEmpty() throws IllegalAccessException {
        assertThat(myJson.toJSON(new ArrayList<Integer>())).isEqualTo(gson.toJson(new ArrayList<Integer>()));
    }

    @Test
    public void testCollectionFull() throws IllegalAccessException {
        assertThat(myJson.toJSON(Collections.singletonList(13))).isEqualTo(gson.toJson(Collections.singletonList(13)));
    }

    @Test
    public void testObject() throws IllegalAccessException {
        assertThat(myJson.toJSON(new Other2())).isEqualTo(gson.toJson(new Other2()));
    }

    @Test
    public void testComplexObject() throws IllegalAccessException {
        assertThat(myJson.toJSON(new Person())).isEqualTo(gson.toJson(new Person()));
    }

    @Test
    public void testArrayObjects() throws IllegalAccessException {
        assertThat(myJson.toJSON(new Person[]{new Person(), new Person()}))
                .isEqualTo(gson.toJson(new Person[]{new Person(), new Person()}));
    }

}