package com.ll.grabit.boundedcontext.address.service;

import com.ll.grabit.boundedcontext.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;


    public List<String> searchAddress2(String address1) {
        return addressRepository.address2List(address1);
    }
    public List<String> searchAddress3(String address2) {
        return addressRepository.address3List(address2);
    }
}
