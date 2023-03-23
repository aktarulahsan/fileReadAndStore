package com.demo.demo.customer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class CustomerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(  name = "id")
    int id;

    @Column(  name = "mailId")
    String mail;
    @Column(  name = "name")
    String name;
    @Column(  name = "branch")
    String branch;
    @Column(  name = "city")
    String city;
    @Column(  name = "state")
    String state;
    @Column(  name = "zipCode")
    String zipCode;
    @Column(  name = "phone")
    String phone;
    @Column(  name = "ipNumber")
    String ip_number;

   /* public CustomerModel(String mail, String name, String branch, String city, String state, String zipCode, String phone, String mobile) {
        this.mail = mail;
        this.name = name;
        this.branch = branch;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phone = phone;
        this.mobile = mobile;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerModel user = (CustomerModel) o;
        return Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }
}
