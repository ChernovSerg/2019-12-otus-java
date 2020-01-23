package ru.otus.chernovsa.diyarraylist;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

public class DiyArrayListTest {
    DIYArrayList<Integer> srcArray;

    @Before
    public void init() {
        srcArray = new DIYArrayList<>();
    }

    @Test
    public void getNullElement() {
        srcArray.add(5);
        srcArray.add(4);
        assertThat(srcArray.get(3)).isNull();
    }

    @Test
    public void testAdd_and_GetExistingElement() {
        srcArray.add(5);
        srcArray.add(4);
        srcArray.add(3);
        assertThat(srcArray.get(1)).isEqualTo(4);
    }

    @Test
    public void size() {
        srcArray.add(5);
        srcArray.add(4);
        srcArray.add(3);
        srcArray.add(2);
        srcArray.add(1);
        assertThat(srcArray.size()).isEqualTo(5);
    }

    @Test
    public void containsTrue() {
        srcArray.add(5);
        srcArray.add(4);
        assertThat(srcArray.contains(4)).isTrue();
    }

    @Test
    public void containsFalse() {
        srcArray.add(5);
        srcArray.add(4);
        assertThat(srcArray.contains(6)).isFalse();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void hasIndexOutOfBoundException() {
        srcArray.add(5);
        srcArray.get(100);
    }

    @Test
    public void testCollectionsAddAll() {
        Integer[] integers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
        Collections.addAll(srcArray, integers);
        for (int i = 0; i < integers.length; i++) {
            assertThat(srcArray.get(i)).isEqualTo(integers[i]);
        }
    }

    @Test
    public void goodSet() {
        srcArray.add(3);
        srcArray.add(4);
        srcArray.add(5);
        srcArray.set(2, 100);
        assertThat(srcArray.get(2)).isEqualTo(100);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void badSet() {
        srcArray.add(3);
        srcArray.add(4);
        srcArray.set(2, 100);
    }

    @Test
    public void testCollectionsCopy() {
        Integer[] array1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
        Integer[] array2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0, 0, 0};
        DIYArrayList<Integer> dest = new DIYArrayList<>();
        Collections.addAll(srcArray, array1);
        Collections.addAll(dest, array2);
        Collections.copy(dest, srcArray);
        for (int i = 0; i < array1.length; i++) {
            assertThat(dest.get(i)).isEqualTo(srcArray.get(i));
        }
    }

    @Test
    public void testCollectionsSort() {
        Integer[] array1 = {4, 2, 3, 1, 6, 5, 10, 9, 8, 7, 11, 14, 13, 12, 15, 18, 17, 16, 21, 20, 19};
        Integer[] array2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
        Collections.addAll(srcArray, array1);
        Collections.sort(srcArray, Integer::compareTo);
        for (int i = 0; i < array1.length; i++) {
            assertThat(srcArray.get(i)).isEqualTo(array2[i]);
        }
    }

}