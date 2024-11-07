package com.example.authSecurity.repository;

import com.example.authSecurity.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
