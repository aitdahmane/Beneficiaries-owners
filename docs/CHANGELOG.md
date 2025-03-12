# CHANGELOG

---

## [v1.0.0] - MUST HAVE Contract 
*Fulfills the mandatory requirements for the `GET /companies/{id}/beneficial-owners` endpoint as defined in the MUST HAVE contract.*

### Added
- **`GET /companies/{id}/beneficial-owners`**
    - **Input**:
        - `id`: Company identifier (path parameter)
        - `filter`: Query parameter (`ALL`, `PHYSICAL_PERSONS`, `EFFECTIVE_OWNERS`)
    - **Output**:
        - `200 OK`: List of beneficiaries with capital ownership percentages (direct/indirect)
        - `404 Not Found`: Company does not exist
        - `204 No Content`: No beneficiaries match the criteria
    - **Functionality**:
        - Recursive ownership calculation with cycle detection.
        - Filters:
            - `ALL`: Direct beneficiaries (companies + persons).
            - `INDIVEDUEL`: Only direct human beneficiaries.
            - `EFFECTIVE`: Persons with >25% direct/indirect ownership.

----

**Future Roadmap**:
- **`POST /companies`**
    - **Input**: `{ "name": "Company ABC" }`
    - **Output**:
        - `201 Created`: Location header with auto-generated company ID.
        - `400 Bad Request`: Invalid payload (e.g., empty name).

- **`POST /persons`**
    - **Input**: `{ "firstName": "John", "lastName": "Doe", "birthDate": "1990-01-01" }`
    - **Output**:
        - `201 Created`: Location header with auto-generated person ID.
        - `400 Bad Request`: Missing required fields.

- **`POST /companies/{id}/beneficial-owners`**
    - **Input**: `{ "beneficiaryId": "123", "type": "PERSON", "percentage": 30.5 }`
    - **Output**:
        - `201 Created`: Success (no body).
        - `404 Not Found`: Company or beneficiary does not exist.
        - `400 Bad Request`: Total ownership exceeds 100% or invalid percentage.  