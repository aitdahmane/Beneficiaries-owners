package com.bpifrance.beneficiariesmanagment.service;

import com.bpifrance.beneficiariesmanagment.dto.OwnerDTO;
import com.bpifrance.beneficiariesmanagment.entity.Company;
import com.bpifrance.beneficiariesmanagment.entity.Owner;
import com.bpifrance.beneficiariesmanagment.entity.Ownership;
import com.bpifrance.beneficiariesmanagment.entity.Person;
import com.bpifrance.beneficiariesmanagment.repository.CompanyRepository;
import com.bpifrance.beneficiariesmanagment.repository.OwnershipRepository;
import com.bpifrance.beneficiariesmanagment.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OwnershipCalculator {

    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;
    private final OwnershipRepository ownershipRepository;

    public List<OwnerDTO> getAllOwners(Company company) {
        Company freshCompany = companyRepository.findById(company.getId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        List<OwnerDTO> owners = new ArrayList<>();
        for (Ownership ownership : freshCompany.getOwners()) {
            Owner owner = ownership.getOwner();
            if (owner instanceof Person) {
                Person person = (Person) owner;
                owners.add(new OwnerDTO(person.getId(), person.getName(), "PERSON", ownership.getPercentage()));
            } else if (owner instanceof Company) {
                Company ownerCompany = (Company) owner;
                owners.add(new OwnerDTO(ownerCompany.getId(), ownerCompany.getName(), "COMPANY", ownership.getPercentage()));
            }
        }
        return owners;
    }

    public List<OwnerDTO> getIndividualOwners(Company company) {
        Company freshCompany = companyRepository.findById(company.getId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        List<OwnerDTO> owners = new ArrayList<>();
        Map<Person, Float> individualOwnerships = new HashMap<>();
        calculateRecursive(freshCompany, 100.0f, individualOwnerships, new HashSet<>());

        for (Map.Entry<Person, Float> entry : individualOwnerships.entrySet()) {
            owners.add(new OwnerDTO(entry.getKey().getId(), entry.getKey().getName(), "PERSON", entry.getValue()));
        }
        return owners;
    }

    public List<OwnerDTO> getEffectiveOwners(Company company) {
        Map<Person, Float> effectiveOwnership = calculateEffectiveOwnership(company);
        List<OwnerDTO> owners = new ArrayList<>();

        for (Map.Entry<Person, Float> entry : effectiveOwnership.entrySet()) {
            owners.add(new OwnerDTO(entry.getKey().getId(), entry.getKey().getName(), "PERSON", entry.getValue()));
        }
        return owners;
    }



    public OwnershipCalculator(CompanyRepository companyRepository,
                               PersonRepository personRepository,
                               OwnershipRepository ownershipRepository) {
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
        this.ownershipRepository = ownershipRepository;
    }

    public Map<Person, Float> calculateEffectiveOwnership(Company company) {
        // Fetch fresh company data from database
        Company freshCompany = companyRepository.findById(company.getId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        Map<Person, Float> results = new HashMap<>();
        calculateRecursive(freshCompany, 100.0f, results, new HashSet<>());
        return results.entrySet().stream()
                .filter(e -> e.getValue() > 25.0f)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private void calculateRecursive(Owner owner, float multiplier, Map<Person, Float> results, Set<UUID> visited) {
        if (!visited.add(owner.getId())) {
            return;
        }

        if (owner instanceof Person) {
            Person person = personRepository.findById(owner.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Person not found"));
            results.merge(person, multiplier, Float::sum);
        } else if (owner instanceof Company) {
            Company company = companyRepository.findById(owner.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Company not found"));
            if (company.getOwners() != null) {
                for (Ownership ownership : company.getOwners()) {
                    float newMultiplier = multiplier * (ownership.getPercentage() / 100.0f);
                    calculateRecursive(ownership.getOwner(), newMultiplier, results, new HashSet<>(visited));
                }
            }
        }
    }


}
