package ru.otus.chernovsa.core.model;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
public class AddressDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private long id;

    @Column(name = "STREET")
    private String street;

    @OneToOne(mappedBy = "address")
    private User user;

    public AddressDataSet() {
    }

    public AddressDataSet(long id, String street) {
        this.id = id;
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{"
                + " id=" + id
                + ", street='" + street + '\''
//                + ", user=" + user
                + '}';
    }
}
