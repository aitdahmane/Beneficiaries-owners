package com.bpifrance.beneficiariesmanagment.repository;


import com.bpifrance.beneficiariesmanagment.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
}