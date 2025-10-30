# Smart Ledger Backend

Application backend for Smart Ledger – Intelligent financial management system.

## Technologies Used

- **Java 21** – Programming language
- **Spring Boot 3.4.1** – Java application framework
- **Spring Security** – Authentication and authorization framework
- **Spring Data JPA** – Data access abstraction
- **PostgreSQL** – Relational database
- **Liquibase** – Database versioning management
- **Lombok** – Library to reduce boilerplate code
- **JWT (JSON Web Token)** – Stateless authentication
- **SpringDoc OpenAPI 3** – API documentation (Swagger)
- **Maven** – Build automation tool

## Prerequisites

- Java JDK 21+
- Maven 3.8+
- PostgreSQL 14+
- Docker (optional, to run PostgreSQL in a container)

## Database Setup

### Option 1: Local PostgreSQL

1. Install PostgreSQL
2. Create the database:

```sql
CREATE DATABASE smartledger;
CREATE USER smartledger WITH PASSWORD 'smartledger';
GRANT ALL PRIVILEGES ON DATABASE smartledger TO smartledger;
```

### Option 2: Docker

```bash
docker run --name smartledger-postgres \
  -e POSTGRES_DB=smartledger \
  -e POSTGRES_USER=smartledger \
  -e POSTGRES_PASSWORD=smartledger \
  -p 5432:5432 \
  -d postgres:16
```

## Configuration

The application uses the `src/main/resources/application.yml` file for configuration.

### Environment Variables

You can override configuration using environment variables:

- `SPRING_DATASOURCE_URL` – Database URL
- `SPRING_DATASOURCE_USERNAME` – Database username
- `SPRING_DATASOURCE_PASSWORD` – Database password
- `JWT_SECRET` – Secret key for JWT (minimum 256 bits)

## Installation

```bash
# Clone the repository
cd smart-ledger-be

# Install dependencies and build
mvn clean install
```

## Development

```bash
# Start the application in development mode
mvn spring-boot:run

# The application will be available at http://localhost:8080/api
# Swagger UI will be available at http://localhost:8080/api/swagger-ui.html
```

## Build

```bash
# Production build
mvn clean package

# The JAR file will be generated at target/smart-ledger-be-1.0.0.jar
```

## Tests

```bash
# Run tests
mvn test

# Run tests with coverage
mvn clean test jacoco:report
```

## Project Structure

```
src/main/java/com/smartledger/
├── config/              # Spring configurations
│   ├── SecurityConfig.java
│   └── CorsConfig.java
├── controller/          # REST controllers
│   └── AuthController.java
├── dto/                 # Data Transfer Objects
│   ├── LoginRequest.java
│   └── LoginResponse.java
├── entity/              # JPA entities
│   └── User.java
├── repository/          # Spring Data repositories
│   └── UserRepository.java
├── security/            # Security components
│   ├── JwtUtil.java
│   └── JwtAuthenticationFilter.java
├── service/             # Business logic
│   ├── AuthService.java
│   └── UserDetailsServiceImpl.java
└── SmartLedgerApplication.java

src/main/resources/
├── application.yml      # Application configuration
└── db/changelog/        # Liquibase migrations
    ├── db.changelog-master.xml
    └── changes/
        └── 001-create-users-table.xml
```

## API Endpoints

### Authentication

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin"
}
```

#### Health Check
```http
GET /api/auth/health

Response: "Service is running"
```

### Default Credentials

- **Username**: `admin`
- **Password**: `admin123`

## API Documentation (Swagger/OpenAPI)

The application integrates **SpringDoc OpenAPI 3** for interactive API documentation.

### Accessing Swagger UI

Once the application is running, the interactive documentation is available at:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/api/v3/api-docs.yaml

### Using Swagger UI

1. Open http://localhost:8080/api/swagger-ui.html in your browser
2. Explore the available endpoints organized by tags
3. Test endpoints directly from the interface:
   - Click an endpoint to expand it
   - Click "Try it out"
   - Enter the required parameters
   - Click "Execute"

### JWT Authentication in Swagger

To test protected endpoints:

1. Log in via the `/auth/login` endpoint
2. Copy the token from the response
3. Click the "Authorize" button at the top right
4. Enter: `Bearer {token}` (replace `{token}` with the copied token)
5. Click "Authorize"
6. You can now test all protected endpoints

### Example

```bash
# 1. Login to obtain the token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Response:
# {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...","username":"admin"}

# 2. Use the token for protected requests
curl -X GET http://localhost:8080/api/protected-endpoint \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Configuration

The SpringDoc configuration is located in `application.yml`:

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    operationsSorter: method
    tagsSorter: alpha
```

### Disabling Swagger in Production

To disable Swagger in production, set in `application.yml`:

```yaml
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false
```

Or via environment variables:
```bash
SPRINGDOC_API-DOCS_ENABLED=false
SPRINGDOC_SWAGGER-UI_ENABLED=false
```

## Liquibase

Liquibase automatically manages database migrations at application startup.

### Useful Commands

```bash
# Show migration status
mvn liquibase:status

# Roll back the last migration
mvn liquibase:rollback -Dliquibase.rollbackCount=1

# Generate SQL for migrations
mvn liquibase:updateSQL
```

### Adding a New Migration

1. Create a new XML file in `src/main/resources/db/changelog/changes/`
2. Add the include in the `db.changelog-master.xml` file
3. Restart the application

Example:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                   http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.20.xsd">

    <changeSet id="002-create-transactions-table" author="smartledger">
        <!-- changeset content -->
    </changeSet>
</databaseChangeLog>
```

## Deployment

### Docker Build

Create a `Dockerfile`:

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/smart-ledger-be-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Docker commands:

```bash
# Build the image
docker build -t smart-ledger-be:1.0.0 .

# Run the container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/smartledger \
  -e SPRING_DATASOURCE_USERNAME=smartledger \
  -e SPRING_DATASOURCE_PASSWORD=smartledger \
  -e JWT_SECRET=your-secret-key-here \
  smart-ledger-be:1.0.0
```

### Deploy to Server

```bash
# Copy the JAR to the server
scp target/smart-ledger-be-1.0.0.jar user@server:/opt/smart-ledger/

# Run the application
java -jar /opt/smart-ledger/smart-ledger-be-1.0.0.jar \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/smartledger \
  --spring.datasource.username=smartledger \
  --spring.datasource.password=smartledger
```

### Nginx Configuration (Reverse Proxy)

```nginx
server {
    listen 80;
    server_name api.your-domain.com;

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

## Security

### JWT Configuration

- The JWT token is valid for 24 hours (configurable in `application.yml`)
- The secret key must be changed in production
- Generate a secure key with: `openssl rand -base64 64`

### Best Practices

1. Change the default admin user password
2. Use HTTPS in production
3. Configure rate limiting
4. Implement logging and monitoring
5. Perform regular database backups

## Logging

Logs are configured with the following levels:

- `INFO` – Default level
- `DEBUG` – For troubleshooting (configurable in `application.yml`)
- `ERROR` – For application errors

Configure in `application.yml`:

```yaml
logging:
  level:
    com.smartledger: DEBUG
    org.springframework.security: DEBUG
```

## Monitoring

Spring Boot Actuator can be enabled for monitoring:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Available endpoints at `/actuator`:
- `/health` – Application status
- `/metrics` – Application metrics
- `/info` – Application information

## Future Development

- [ ] Financial transaction management
- [ ] Customizable categories
- [ ] Reports and statistics
- [ ] Data export (CSV, PDF)
- [ ] Email notifications
- [ ] Budget management API
- [ ] Multi-tenancy

## Troubleshooting

### Database Connection Failed

Check that:
1. PostgreSQL is running
2. Credentials are correct
3. The firewall allows connections on port 5432

### JWT Token Invalid

Check that:
1. The token has not expired
2. The secret key is the same used to sign the token
3. The token has the correct format: `Bearer <token>`

## License

All rights reserved © 2025
