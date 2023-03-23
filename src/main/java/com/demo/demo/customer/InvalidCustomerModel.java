package com.demo.demo.customer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "invalidCustomer")
public class InvalidCustomerModel {

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

    public InvalidCustomerModel() {
    }

    public InvalidCustomerModel(int id, String name, String branch, String city, String state, String zipCode, String phone,String mail, String ip_number) {
//        this.id = id;
        this.mail = mail;
        this.name = name;
        this.branch = branch;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phone = phone;
        this.ip_number = ip_number;
    }
}
