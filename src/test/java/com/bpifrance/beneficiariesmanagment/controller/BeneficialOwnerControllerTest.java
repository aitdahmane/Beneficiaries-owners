package com.bpifrance.beneficiariesmanagment.controller;

import com.bpifrance.beneficiariesmanagment.dto.OwnerDTO;
import com.bpifrance.beneficiariesmanagment.entity.Company;
import com.bpifrance.beneficiariesmanagment.enums.FilterType;
import com.bpifrance.beneficiariesmanagment.repository.CompanyRepository;
import com.bpifrance.beneficiariesmanagment.service.OwnershipCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BeneficialOwnerControllerTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private OwnershipCalculator ownershipCalculator;

    private BeneficialOwnerController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new BeneficialOwnerController(companyRepository, ownershipCalculator);
    }

    @Test
    void api_EffectiveFilter_ReturnsOnlyBEs() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        Company company = new Company();
        company.setId(companyId);

        OwnerDTO owner1 = new OwnerDTO();
        owner1.setName("Effective Owner");
        owner1.setOwnershipPercentage(30.0f);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(ownershipCalculator.getEffectiveOwners(company)).thenReturn(Arrays.asList(owner1));

        // Act
        ResponseEntity<?> response = controller.getBeneficialOwners(companyId, FilterType.EFFECTIVE);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((java.util.List<?>) response.getBody()).size());
    }

    @Test
    void api_IndividualsFilter_ExcludesCompanies() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        Company company = new Company();
        company.setId(companyId);

        OwnerDTO individualOwner = new OwnerDTO();
        individualOwner.setName("Individual Person");
        individualOwner.setOwnershipPercentage(25.0f);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(ownershipCalculator.getIndividualOwners(company)).thenReturn(Arrays.asList(individualOwner));

        // Act
        ResponseEntity<?> response = controller.getBeneficialOwners(companyId, FilterType.INDIVIDUALS);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((java.util.List<?>) response.getBody()).size());
    }

    @Test
    void noBeneficialOwners_Returns204() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        Company company = new Company();
        company.setId(companyId);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(ownershipCalculator.getEffectiveOwners(company)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<?> response = controller.getBeneficialOwners(companyId, FilterType.EFFECTIVE);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void companyNotFound_Returns404() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        when(companyRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = controller.getBeneficialOwners(companyId, FilterType.EFFECTIVE);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Company not found with id: " + companyId, response.getBody());
    }
}