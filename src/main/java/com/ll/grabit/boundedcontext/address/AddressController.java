package com.ll.grabit.boundedcontext.address;

import com.ll.grabit.boundedcontext.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/search/address2")
    @ResponseBody
    public List<String> searchAddress2(String address1){
        List<String> searchAddress2 = addressService.searchAddress2(address1);
        return searchAddress2;
    }


    @GetMapping("/search/address3")
    @ResponseBody
    public List<String> searchAddress3(String address1, String address2){
        List<String> searchAddress3 = addressService.searchAddress3(address1, address2);
        return searchAddress3;
    }
}
