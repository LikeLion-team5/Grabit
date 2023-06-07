package com.ll.grabit.boundedcontext.address.service;

import com.ll.grabit.boundedcontext.address.dto.AddressSearchDto;
import com.ll.grabit.boundedcontext.address.entity.Address;
import com.ll.grabit.boundedcontext.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<String> getAddress1List() {
        List<String> address1List = addressRepository.address1List();
        return address1List;
    }

//    public List<Address> getAddressList(AddressSearchDto addressSearchDto){
//        return filteredAddress(addressSearchDto.getAddress1(), addressSearchDto.getAddress2(), addressSearchDto.getAddress3());
//    }

//    private List<Address> filteredAddress(String address1, String address2, String address3) {
//        List<Address> addressList = new ArrayList<>();
//        if (address3 != null && !address3.isEmpty()) {
//            addressList = addressRepository.findAddress3(address1, address2, address3);
//        }
//        else if (address2 != null && !address2.isEmpty()) {
//            addressList = addressRepository.findAddress2(address1, address2);
//        }
//        else if(address1 != null && !address1.isEmpty()) {
//            addressList = addressRepository.findAddress1(address1);
//        }
//
//        return addressList;
//    }
}
