package ru.otus.chernovsa.core.model;

import ru.otus.chernovsa.core.dao.Id;

public class User {
    @Id
    private final long idnt;
    private final int age;
    private String name;

    public User() {
        this.idnt = 0;
        this.age = 0;
        this.name = "";
    }

    public User(long id, int age, String name) {
        this.idnt = id;
        this.age = age;
        this.name = name;
    }

    public long getIdnt() {
        return idnt;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + idnt +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
