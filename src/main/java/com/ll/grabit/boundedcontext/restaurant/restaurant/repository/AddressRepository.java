package com.ll.grabit.boundedcontext.restaurant.restaurant.repository;

import com.ll.grabit.boundedcontext.restaurant.restaurant.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByAddress1AndAddress2AndAddress3(String address1, String address2, String address3);
}
