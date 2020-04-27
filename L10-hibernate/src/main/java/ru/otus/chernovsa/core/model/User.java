package ru.otus.chernovsa.core.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME")
    private String name;

    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "ADDRESS_ID")
    private AddressDataSet address;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Collection<PhoneDataSet> phones;

    public User() {
    }

    public User(long id, String name, AddressDataSet address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = new ArrayList<>();
    }

    public User(long id, String name, AddressDataSet address, Collection<PhoneDataSet> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public Collection<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(Collection<PhoneDataSet> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "User{"
                + " id=" + id
                + ", name='" + name + '\''
                + ", address=" + address
                + ", phones=" + phones.stream().map(PhoneDataSet::toString).collect(Collectors.joining(", "))
                + '}';
    }
}
