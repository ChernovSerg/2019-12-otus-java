package ru.otus.chernovsa.diyarraylist;

import java.lang.reflect.Array;
import java.util.*;

public class DIYArrayList<T> implements List<T> {
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10; //я только потом увидел, что в ArrayList такое же значение по умолчанию ))
    private static final int RESIZE_FACTOR = 2;
    private int capacity;
    private T[] array;

    @SuppressWarnings("unchecked")
    public DIYArrayList() {
        this.capacity = DEFAULT_CAPACITY;
        this.array = (T[]) new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    public DIYArrayList(int size) {
        this.capacity = Math.max(size, DEFAULT_CAPACITY);
        this.array = (T[]) new Object[capacity];
    }

    private class DIYListIterator implements ListIterator<T> {
        private int cursor = -1;
        private T node;

        DIYListIterator() {

        }

        @Override
        public boolean hasNext() {
            return  size > 0 && cursor < size;
        }

        @Override
        public T next() {
            if (hasNext()) {
                return get(++cursor);
            }
            throw new NoSuchElementException();
        }

        @Override
        public void set(T t) {
            if (cursor >= 0) {
                DIYArrayList.this.set(cursor, t);
            }
        }

        @Override
        public void add(T t) {

        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public T previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {

        }
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        capacity *= RESIZE_FACTOR;
        T[] dest = (T[]) new Object[capacity];
        System.arraycopy(array, 0, dest, 0, array.length);
        this.array = dest;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size <= 0;
    }

    @Override
    public boolean contains(Object o) {
        boolean result = false;
        if (o != null) {
            for (int i = 0; i < this.size; i++) {
                if (array[i].equals(o)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, this.size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (this.size == this.capacity) {
            grow();
        }
        array[size++] = (T) t;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= capacity) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.array[index];
    }

    @Override
    public T set(int index, T element) {
        T old;
        if (size > 0 && index < size) {
            old = array[index];
            array[index] = element;
            return old;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
}
