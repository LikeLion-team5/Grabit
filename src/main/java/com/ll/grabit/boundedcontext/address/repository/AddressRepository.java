package com.ll.grabit.boundedcontext.address.repository;

import com.ll.grabit.boundedcontext.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByAddress1AndAddress2AndAddress3(String address1, String address2, String address3);

    @Query("select a from Address a where a.address1 = :address1")
    List<Address> findAddress1(@Param("address1") String address1);
    @Query("select a from Address a where a.address1 = :address1 and a.address2 = :address2")
    List<Address> findAddress2(@Param("address1")String address1, @Param("address2")String address2);
    @Query("select a from Address a where a.address1 = :address1 and a.address2 = :address2 and a.address3 = :address3")
    List<Address> findAddress3(@Param("address1")String address1, @Param("address2")String address2, @Param("address3")String address3);
}
