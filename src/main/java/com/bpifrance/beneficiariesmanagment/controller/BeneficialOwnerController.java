package com.bpifrance.beneficiariesmanagment.controller;

import com.bpifrance.beneficiariesmanagment.dto.OwnerDTO;
import com.bpifrance.beneficiariesmanagment.enums.FilterType;
import com.bpifrance.beneficiariesmanagment.repository.CompanyRepository;
import com.bpifrance.beneficiariesmanagment.service.OwnershipCalculator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Beneficial Owners", description = "API endpoints for managing beneficial owners of companies")
public class BeneficialOwnerController {

    private final CompanyRepository companyRepository;
    private final OwnershipCalculator ownershipCalculator;

    public BeneficialOwnerController(CompanyRepository companyRepository,
                                     OwnershipCalculator ownershipCalculator) {
        this.companyRepository = companyRepository;
        this.ownershipCalculator = ownershipCalculator;
    }

    @Operation(summary = "Get beneficial owners of a company",
            description = "Retrieves a list of beneficial owners for a specific company based on the specified filter type. " +
                    "The filter type can be ALL (all owners), INDIVIDUALS (only individual owners), or EFFECTIVE (owners with >25% ownership)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of owners",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OwnerDTO.class))),
            @ApiResponse(responseCode = "204", description = "No owners found for the company",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid filter type or other bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/companies/{id}/beneficial-owners")
    public ResponseEntity<?> getBeneficialOwners(
            @Parameter(description = "ID of the company", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Type of owners to filter (ALL, INDIVIDUALS, or EFFECTIVE)", required = false,
                    schema = @Schema(implementation = FilterType.class, defaultValue = "EFFECTIVE"))
            @RequestParam(required = false, defaultValue = "EFFECTIVE") FilterType filterType) {

        return companyRepository.findById(id)
                .map(company -> {
                    try {
                        List<OwnerDTO> owners;
                        switch (filterType) {
                            case ALL:
                                owners = ownershipCalculator.getAllOwners(company);
                                break;
                            case INDIVIDUALS:
                                owners = ownershipCalculator.getIndividualOwners(company);
                                break;
                            case EFFECTIVE:
                                owners = ownershipCalculator.getEffectiveOwners(company);
                                break;
                            default:
                                return ResponseEntity.badRequest().body("Invalid filter type");
                        }

                        if (owners.isEmpty()) {
                            return ResponseEntity.noContent().build();
                        }

                        return ResponseEntity.ok(owners);
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Company not found with id: " + id));
    }
}