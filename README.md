# Name Insight API

A Spring Boot REST API that accepts a name and returns enriched profile data by combining results from three external prediction APIs: Genderize, Agify, and Nationalize.

---

## Tech Stack

- **Java 21**
- **Spring Boot 4.x**
- **Spring Data JPA** + **Hibernate**
- **H2** (local development)
- **PostgreSQL** (production)
- **Lombok**
- **UUID Creator** (UUID v7)

---

## How It Works

When a name is submitted via `POST /api/profiles`:

1. Checks if a profile for that name already exists (case-insensitive)
2. If yes → returns the existing profile with a `"Profile already exists"` message
3. If no → calls Genderize, Agify, and Nationalize in parallel
4. Validates the responses (null/empty data → 502)
5. Classifies age into an age group
6. Picks the top country by probability
7. Saves and returns the new profile

---

## API Endpoints

### `POST /api/profiles`
Creates a new profile or returns an existing one.

**Request body:**
```json
{ "name": "emma" }
```

**Response (201 — created):**
```json
{
  "status": "success",
  "data": {
    "id": "019d9c9a-8471-7150-a9b3-71d8f33608f6",
    "name": "emma",
    "gender": "female",
    "gender_probability": 0.97,
    "sample_size": 500304,
    "age": 43,
    "age_group": "adult",
    "country_id": "CN",
    "country_probability": 0.09,
    "created_at": "2026-04-17T18:01:05.393106Z"
  }
}
```

**Response (200 — duplicate):**
```json
{
  "status": "success",
  "message": "Profile already exists",
  "data": { ... }
}
```

---

### `GET /api/profiles/{id}`
Returns a single profile by ID.

**Response (200):**
```json
{
  "status": "success",
  "data": { ...full profile... }
}
```

---

### `GET /api/profiles`
Returns all profiles. Supports optional filters.

**Query parameters (all optional, case-insensitive):**
| Param | Example |
|-------|---------|
| `gender` | `?gender=female` |
| `country_id` | `?country_id=US` |
| `age_group` | `?age_group=adult` |

**Response (200):**
```json
{
  "status": "success",
  "data": [
    {
      "id": "...",
      "name": "emma",
      "gender": "female",
      "age": 43,
      "age_group": "adult",
      "country_id": "CN"
    }
  ],
  "count": 1
}
```

---

### `DELETE /api/profiles/{id}`
Deletes a profile by ID.

**Response:** `204 No Content`

---

## Error Responses

All errors follow this shape:
```json
{ "status": "error", "message": "descriptive message" }
```

| Status | Cause |
|--------|-------|
| `400` | Missing, null, or empty name |
| `422` | Name is not a string (e.g. a number) |
| `404` | Profile not found |
| `502` | External API returned null or empty data |
| `500` | Unexpected server error |

---

## Classification Logic

**Age groups:**
| Range | Group |
|-------|-------|
| 0–12 | `child` |
| 13–19 | `teenager` |
| 20–59 | `adult` |
| 60+ | `senior` |

**Country:** the entry with the highest probability from the Nationalize response.

---

## Running Locally

### Prerequisites
- Java 21+
- Maven

### Steps

```bash
# Clone the repo
git clone https://github.com/wendy-wej/name-insight-api.git
cd name-insight-api

# Run the app
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

### Test it

```bash
# Create a profile
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{"name": "emma"}'

# Get all profiles
curl http://localhost:8080/api/profiles

# Filter by gender
curl "http://localhost:8080/api/profiles?gender=female"

# Get by ID
curl http://localhost:8080/api/profiles/{id}

# Delete
curl -X DELETE http://localhost:8080/api/profiles/{id}
```

---

## Deployment (Railway)

1. Create a new project on [Railway](https://railway.app)
2. Add a **PostgreSQL** database service
3. Connect your GitHub repo
4. Set the environment variable:
   ```
   SPRING_PROFILES_ACTIVE=prod
   ```
5. Railway automatically injects `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD`

The `application-prod.properties` file picks these up and connects to PostgreSQL automatically.

---

## External APIs Used

| API | URL | Data returned |
|-----|-----|---------------|
| Genderize | `https://api.genderize.io?name={name}` | gender, probability, sample size |
| Agify | `https://api.agify.io?name={name}` | predicted age |
| Nationalize | `https://api.nationalize.io?name={name}` | country probabilities |

All APIs are free and require no authentication.