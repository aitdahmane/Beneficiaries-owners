# DESIGN DECISIONS

This document outlines the key architectural and implementation decisions for the Beneficial Ownership API, focusing on clarity, scalability.

---

## 1. Layered Architecture

```mermaid
flowchart TD
    Client([Client]) -->|HTTP Requests| Controller[[Controllers]]
    Controller -->|Appel| Service[[Services]]
    Service -->|Logique métier| Repository[[Repositories]]
    Repository -->|Stockage| Memory[(In-Memory Storage)]
```

### Rationale

#### Separation of Concerns
Adheres to the classical 2-tier pattern for maintainability and testability:

- **Controllers**: Handle HTTP translation (`@RestController`).
- **Services**: Core business logic (e.g., `OwnershipCalculator`). 


## 2. Data Model

```mermaid
erDiagram
    COMPANY ||--o{ BENEFICIAL_OWNERSHIP : has
    PERSON ||--o{ BENEFICIAL_OWNERSHIP : has
```

### Key Decisions

## 3. Ownership Calculation Algorithm

```mermaid
flowchart TD
    Start([Start]) --> LoadCompany
    LoadCompany --> CheckVisited
    CheckVisited -->|No| AddVisited
    AddVisited --> Iterate
    CheckVisited -->|Yes| Skip
    Iterate --> ForEachOwner
  ForEachOwner --> IsPerson
    IsPerson -->|Yes| AddToResult
    IsPerson -->|No| Recurse
```

### Implementation Details

#### Recursion with Cycle Detection
- Prevents infinite loops (e.g., Company A → Company B → Company A)

#### Percentage Propagation
- Multiplies ownership percentages recursively
- Example:
    - Company A → Company B (30%) → Person X (40%)
    - Effective ownership = 30% * 40% = 12%

#### Threshold Filtering
- Post-process results to retain only owners with `totalPercentage > 25%`

#### Why Not Iterative BFS/DFS?
Recursion simplifies ownership chain modeling.


