package com.ll.grabit.boundedcontext.address.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String address1;
    private String address2;
    private String address3;

    public Address(String address1, String address2, String address3) {
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
    }
}
