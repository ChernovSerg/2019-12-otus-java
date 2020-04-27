package ru.otus.chernovsa.core.model;

import javax.persistence.*;

@Entity
@Table(name = "PHONE")
public class PhoneDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private long id;

    @Column(name = "NUMBER")
    private String number;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;


    public PhoneDataSet() {
    }

    public PhoneDataSet(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public PhoneDataSet(long id, String number, User user) {
        this.id = id;
        this.number = number;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{"
                + " id=" + id
                + ", number='" + number + '\''
                + '}';
    }
}
