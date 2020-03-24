package ru.otus.chernovsa.myjsonwriter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Demo {
    public static void main(String[] args) throws IllegalAccessException {
        Person p = new Person();
        System.out.println("    p: " + p);

        MyJsonWriter myJsonWriter = new MyJsonWriter();
        myJsonWriter.parse(p);
        String json = myJsonWriter.toJSON();

        Gson gson = new Gson();
        Person clone = gson.fromJson(json, Person.class);
        System.out.println("clone: " + clone);
        System.out.println("json: " + json);

        System.out.println("-----");
        System.out.println("p == clone: " + p.equals(clone));
    }
}

class Person {
    private OtherObj other;
    private Integer[] ints;
    private double[] dbls = new double[2];
    private List<String> addresses = new ArrayList<>();
    private List<OtherObj> others = new ArrayList<>();
    private int age;
    private boolean sex;
    private Boolean sexx;
    private Integer weight;
    private String name;
    private Double money;
    private String[] strings = new String[3];
    private char ch;

    public Person() {
        ints = new Integer[3];
        ints[1] = 3;
        dbls[1] = 45.225000001;
        other = new OtherObj("opa", 4);
        others.add(new OtherObj("tuk", 88));
        others.add(new OtherObj("pop", 100));
        age = 20;
        weight = 65;
        name = "Serg";
        addresses.add("Khimki, Mashintseva, 9 , 2");
        addresses.add(null);
        addresses.add("Moscow, Solovjinaya roscha, 1 , 25");
        money = 7000D;
        strings[0] = "qwe";
        strings[1] = "asd";
        strings[2] = "zxc";
        ch = 'R';
        sex = true;
        sexx = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;

        return age == person.age &&
                sex == person.sex &&
                ch == person.ch &&
                Objects.equals(other, person.other) &&
                Arrays.equals(ints, person.ints) &&

                dbls.length == person.dbls.length &&
                Math.abs(Arrays.stream(dbls).sum() - Arrays.stream(person.dbls).sum()) < 0.005*dbls.length &&

                Objects.equals(addresses, person.addresses) &&
                Objects.equals(others, person.others) &&
                Objects.equals(sexx, person.sexx) &&
                Objects.equals(weight, person.weight) &&
                Objects.equals(name, person.name) &&
                Math.abs(money - person.money) < 0.01 &&
                Arrays.equals(strings, person.strings);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(other, addresses, others, age, sex, sexx, weight, name, money, ch);
        result = 31 * result + Arrays.hashCode(ints);
        result = 31 * result + Arrays.hashCode(dbls);
        result = 31 * result + Arrays.hashCode(strings);
        return result;
    }

    @Override
    public String toString() {
        return "Person{"
                + " other=" + other
                + ", ints=" + Arrays.toString(ints)
                + ", dbls=" + Arrays.toString(dbls)
                + ", addresses=" + addresses
                + ", others=" + others
                + ", age=" + age
                + ", sex=" + sex
                + ", sexx=" + sexx
                + ", weight=" + weight
                + ", name='" + name + '\''
                + ", money=" + money
                + ", strings=" + Arrays.toString(strings)
                + ", ch=" + ch
                + "}\n";
    }
}

class OtherObj {
    private String str;
    private Integer k;
    private Other2 other2 = new Other2();

    public OtherObj(String str, Integer k) {
        this.str = str;
        this.k = k;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OtherObj)) return false;
        OtherObj otherObj = (OtherObj) o;
        return Objects.equals(str, otherObj.str) &&
                Objects.equals(k, otherObj.k) &&
                Objects.equals(other2, otherObj.other2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str, k, other2);
    }

    @Override
    public String toString() {
        return "OtherObj{"
                + " str='" + str + '\''
                + ", k=" + k
                + ", other2=" + other2
                + '}';
    }
}

class Other2 {
    private int n = 5;
    private boolean flag = true;
    List<Integer> list = Arrays.asList(1, 2, 3);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Other2)) return false;
        Other2 other2 = (Other2) o;
        return n == other2.n &&
                flag == other2.flag &&
                list.equals(other2.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, flag, list);
    }

    @Override
    public String toString() {
        return "Other2{"
                + " n=" + n
                + ", flag=" + flag
                + ", list=" + list
                + '}';
    }
}

