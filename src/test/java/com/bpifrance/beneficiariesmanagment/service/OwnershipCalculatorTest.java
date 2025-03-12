package com.bpifrance.beneficiariesmanagment.service;

import com.bpifrance.beneficiariesmanagment.entity.Company;
import com.bpifrance.beneficiariesmanagment.entity.Ownership;
import com.bpifrance.beneficiariesmanagment.entity.Person;
import com.bpifrance.beneficiariesmanagment.repository.CompanyRepository;
import com.bpifrance.beneficiariesmanagment.repository.OwnershipRepository;
import com.bpifrance.beneficiariesmanagment.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class OwnershipCalculatorTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private OwnershipRepository ownershipRepository;

    private OwnershipCalculator ownershipCalculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ownershipCalculator = new OwnershipCalculator(companyRepository, personRepository, ownershipRepository);
    }

    @Test
    void directOwnershipOver25_ShouldReturnBE() {
        // Arrange
        Company company = new Company();
        company.setId(UUID.randomUUID());

        Person person = new Person();
        person.setId(UUID.randomUUID());

        Ownership ownership = new Ownership();
        ownership.setOwner(person);
        ownership.setPercentage(30.0f);
        ownership.setCompany(company);

        company.setOwners(Arrays.asList(ownership));

        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        // Act
        Map<Person, Float> result = ownershipCalculator.calculateEffectiveOwnership(company);

        // Assert
        assertEquals(1, result.size());
        assertEquals(30.0f, result.get(person),0.000002f);
    }

    @Test
    void indirectOwnership_ShouldAggregate() {
        // Arrange
        Company mainCompany = new Company();
        mainCompany.setId(UUID.randomUUID());

        Company intermediateCompany = new Company();
        intermediateCompany.setId(UUID.randomUUID());

        Person person = new Person();
        person.setId(UUID.randomUUID());

        // Main company is owned 60% by intermediate company
        Ownership mainOwnership = new Ownership();
        mainOwnership.setOwner(intermediateCompany);
        mainOwnership.setPercentage(60.0f);
        mainOwnership.setCompany(mainCompany);
        mainCompany.setOwners(Arrays.asList(mainOwnership));

        // Intermediate company is owned 50% by person
        Ownership intermediateOwnership = new Ownership();
        intermediateOwnership.setOwner(person);
        intermediateOwnership.setPercentage(50.0f);
        intermediateOwnership.setCompany(intermediateCompany);
        intermediateCompany.setOwners(Arrays.asList(intermediateOwnership));

        when(companyRepository.findById(mainCompany.getId())).thenReturn(Optional.of(mainCompany));
        when(companyRepository.findById(intermediateCompany.getId())).thenReturn(Optional.of(intermediateCompany));
        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        // Act
        Map<Person, Float> result = ownershipCalculator.calculateEffectiveOwnership(mainCompany);

        // Assert
        assertEquals(1, result.size());
        assertEquals(30.0f, result.get(person), 0.000002f); // 60% * 50% = 30%
    }

    @Test
    void combinedOwnership_ShouldSum() {
        // Arrange
        Company company = new Company();
        company.setId(UUID.randomUUID());

        Company intermediateCompany = new Company();
        intermediateCompany.setId(UUID.randomUUID());

        Person person = new Person();
        person.setId(UUID.randomUUID());

        // Direct ownership 20%
        Ownership directOwnership = new Ownership();
        directOwnership.setOwner(person);
        directOwnership.setPercentage(20.0f);
        directOwnership.setCompany(company);

        // Indirect ownership through intermediate company (30% * 50% = 15%)
        Ownership indirectOwnership = new Ownership();
        indirectOwnership.setOwner(intermediateCompany);
        indirectOwnership.setPercentage(30.0f);
        indirectOwnership.setCompany(company);

        company.setOwners(Arrays.asList(directOwnership, indirectOwnership));

        // Intermediate company ownership
        Ownership intermediateOwnership = new Ownership();
        intermediateOwnership.setOwner(person);
        intermediateOwnership.setPercentage(50.0f);
        intermediateOwnership.setCompany(intermediateCompany);
        intermediateCompany.setOwners(Arrays.asList(intermediateOwnership));

        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
        when(companyRepository.findById(intermediateCompany.getId())).thenReturn(Optional.of(intermediateCompany));
        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        // Act
        Map<Person, Float> result = ownershipCalculator.calculateEffectiveOwnership(company);

        // Assert
        assertEquals(1, result.size());
        assertEquals(35.0f, result.get(person)); // Direct 20% + Indirect 15% (30% * 50%)
    }
}