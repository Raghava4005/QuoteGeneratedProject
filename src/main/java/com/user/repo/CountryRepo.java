package com.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entities.Country;

public interface CountryRepo extends JpaRepository<Country, Integer> {

}
