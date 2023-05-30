package com.ll.grabit.base.initdata;

import com.ll.grabit.boundedcontext.restaurant.entity.Address;
import com.ll.grabit.boundedcontext.restaurant.repository.AddressRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Configuration
public class AddressInitData {
    @Bean
    CommandLineRunner initAddressData(AddressRepository addressRepository) {
        return args -> {

            //address 저장
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new ClassPathResource("data/address_seoul.csv").getInputStream(),
                            StandardCharsets.UTF_8
                    )
            )) {
                reader.lines()
                        .skip(1) // Skip the header row
                        .map(line -> line.split(","))
                        .map(columns -> {
                            String address1 = columns[0];
                            String address2 = columns.length > 1 ? columns[1] : "";
                            String address3 = columns.length > 2 ? columns[2] : "";
                            return new Address(address1, address2, address3);
                        })
                        .forEach(addressRepository::save);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //식당 저장
        };
    }
}
