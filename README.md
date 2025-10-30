# Smart Ledger Backend

Applicazione backend per Smart Ledger - Sistema di gestione finanziaria intelligente.

## Tecnologie Utilizzate

- **Java 21** - Linguaggio di programmazione
- **Spring Boot 3.4.1** - Framework per applicazioni Java
- **Spring Security** - Framework per autenticazione e autorizzazione
- **Spring Data JPA** - Astrazione per l'accesso ai dati
- **PostgreSQL** - Database relazionale
- **Liquibase** - Gestione versioning del database
- **Lombok** - Libreria per ridurre il boilerplate code
- **JWT (JSON Web Token)** - Autenticazione stateless
- **SpringDoc OpenAPI 3** - Documentazione API (Swagger)
- **Maven** - Build automation tool

## Prerequisiti

- Java JDK 21+
- Maven 3.8+
- PostgreSQL 14+
- Docker (opzionale, per eseguire PostgreSQL in container)

## Setup Database

### Opzione 1: PostgreSQL Locale

1. Installare PostgreSQL
2. Creare il database:

```sql
CREATE DATABASE smartledger;
CREATE USER smartledger WITH PASSWORD 'smartledger';
GRANT ALL PRIVILEGES ON DATABASE smartledger TO smartledger;
```

### Opzione 2: Docker

```bash
docker run --name smartledger-postgres \
  -e POSTGRES_DB=smartledger \
  -e POSTGRES_USER=smartledger \
  -e POSTGRES_PASSWORD=smartledger \
  -p 5432:5432 \
  -d postgres:16
```

## Configurazione

L'applicazione utilizza il file `src/main/resources/application.yml` per la configurazione.

### Variabili d'Ambiente

È possibile sovrascrivere la configurazione usando variabili d'ambiente:

- `SPRING_DATASOURCE_URL` - URL del database
- `SPRING_DATASOURCE_USERNAME` - Username del database
- `SPRING_DATASOURCE_PASSWORD` - Password del database
- `JWT_SECRET` - Chiave segreta per JWT (minimo 256 bit)

## Installazione

```bash
# Clonare il repository
cd smart-ledger-be

# Installare le dipendenze e compilare
mvn clean install
```

## Sviluppo

```bash
# Avviare l'applicazione in modalità sviluppo
mvn spring-boot:run

# L'applicazione sarà disponibile su http://localhost:8080/api
# Swagger UI sarà disponibile su http://localhost:8080/api/swagger-ui.html
```

## Build

```bash
# Build di produzione
mvn clean package

# Il file JAR sarà generato in target/smart-ledger-be-1.0.0.jar
```

## Test

```bash
# Eseguire i test
mvn test

# Eseguire i test con coverage
mvn clean test jacoco:report
```

## Struttura del Progetto

```
src/main/java/com/smartledger/
├── config/              # Configurazioni Spring
│   ├── SecurityConfig.java
│   └── CorsConfig.java
├── controller/          # REST Controllers
│   └── AuthController.java
├── dto/                 # Data Transfer Objects
│   ├── LoginRequest.java
│   └── LoginResponse.java
├── entity/              # JPA Entities
│   └── User.java
├── repository/          # Spring Data Repositories
│   └── UserRepository.java
├── security/            # Componenti di sicurezza
│   ├── JwtUtil.java
│   └── JwtAuthenticationFilter.java
├── service/             # Business Logic
│   ├── AuthService.java
│   └── UserDetailsServiceImpl.java
└── SmartLedgerApplication.java

src/main/resources/
├── application.yml      # Configurazione applicazione
└── db/changelog/        # Liquibase migrations
    ├── db.changelog-master.xml
    └── changes/
        └── 001-create-users-table.xml
```

## API Endpoints

### Autenticazione

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

### Credenziali di Default

- **Username**: `admin`
- **Password**: `admin123`

## Documentazione API (Swagger/OpenAPI)

L'applicazione integra **SpringDoc OpenAPI 3** per la documentazione interattiva delle API.

### Accesso a Swagger UI

Una volta avviata l'applicazione, la documentazione interattiva è disponibile a:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/api/v3/api-docs.yaml

### Utilizzo di Swagger UI

1. Aprire http://localhost:8080/api/swagger-ui.html nel browser
2. Esplorare gli endpoint disponibili organizzati per tag
3. Testare gli endpoint direttamente dall'interfaccia:
   - Click su un endpoint per espanderlo
   - Click su "Try it out"
   - Inserire i parametri richiesti
   - Click su "Execute"

### Autenticazione con JWT in Swagger

Per testare endpoint protetti:

1. Fare login tramite l'endpoint `/auth/login`
2. Copiare il token dalla risposta
3. Click sul pulsante "Authorize" in alto a destra
4. Inserire: `Bearer {token}` (sostituire `{token}` con il token copiato)
5. Click su "Authorize"
6. Ora puoi testare tutti gli endpoint protetti

### Esempio

```bash
# 1. Login per ottenere il token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Response:
# {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...","username":"admin"}

# 2. Usare il token per le richieste protette
curl -X GET http://localhost:8080/api/protected-endpoint \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Configurazione

La configurazione di SpringDoc si trova in `application.yml`:

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

### Disabilitare Swagger in Produzione

Per disabilitare Swagger in produzione, impostare in `application.yml`:

```yaml
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false
```

O tramite variabile d'ambiente:
```bash
SPRINGDOC_API-DOCS_ENABLED=false
SPRINGDOC_SWAGGER-UI_ENABLED=false
```

## Liquibase

Liquibase gestisce automaticamente le migrazioni del database all'avvio dell'applicazione.

### Comandi Utili

```bash
# Visualizzare lo stato delle migrazioni
mvn liquibase:status

# Rollback dell'ultima migrazione
mvn liquibase:rollback -Dliquibase.rollbackCount=1

# Generare SQL per le migrazioni
mvn liquibase:updateSQL
```

### Aggiungere una Nuova Migrazione

1. Creare un nuovo file XML in `src/main/resources/db/changelog/changes/`
2. Aggiungere l'include nel file `db.changelog-master.xml`
3. Riavviare l'applicazione

Esempio:
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

### Build Docker

Creare un `Dockerfile`:

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/smart-ledger-be-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Comandi Docker:

```bash
# Build dell'immagine
docker build -t smart-ledger-be:1.0.0 .

# Run del container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/smartledger \
  -e SPRING_DATASOURCE_USERNAME=smartledger \
  -e SPRING_DATASOURCE_PASSWORD=smartledger \
  -e JWT_SECRET=your-secret-key-here \
  smart-ledger-be:1.0.0
```

### Deploy su Server

```bash
# Copiare il JAR sul server
scp target/smart-ledger-be-1.0.0.jar user@server:/opt/smart-ledger/

# Eseguire l'applicazione
java -jar /opt/smart-ledger/smart-ledger-be-1.0.0.jar \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/smartledger \
  --spring.datasource.username=smartledger \
  --spring.datasource.password=smartledger
```

### Configurazione Nginx (Reverse Proxy)

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

## Sicurezza

### JWT Configuration

- Il token JWT ha una validità di 24 ore (configurabile in `application.yml`)
- La chiave segreta deve essere cambiata in produzione
- Generare una chiave sicura con: `openssl rand -base64 64`

### Best Practices

1. Cambiare la password di default dell'utente admin
2. Usare HTTPS in produzione
3. Configurare rate limiting
4. Implementare logging e monitoring
5. Backup regolari del database

## Logging

I log sono configurati con i seguenti livelli:

- `INFO` - Livello di default
- `DEBUG` - Per troubleshooting (configurabile in application.yml)
- `ERROR` - Per errori applicativi

Configurare in `application.yml`:

```yaml
logging:
  level:
    com.smartledger: DEBUG
    org.springframework.security: DEBUG
```

## Monitoraggio

Spring Boot Actuator può essere abilitato per il monitoraggio:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Endpoints disponibili su `/actuator`:
- `/health` - Stato dell'applicazione
- `/metrics` - Metriche dell'applicazione
- `/info` - Informazioni sull'applicazione

## Sviluppo Futuro

- [ ] Gestione transazioni finanziarie
- [ ] Categorie personalizzabili
- [ ] Report e statistiche
- [ ] Export dati (CSV, PDF)
- [ ] Notifiche email
- [ ] API per gestione budget
- [ ] Multi-tenancy

## Troubleshooting

### Database Connection Failed

Verificare che:
1. PostgreSQL sia in esecuzione
2. Le credenziali siano corrette
3. Il firewall permetta la connessione sulla porta 5432

### JWT Token Invalid

Verificare che:
1. Il token non sia scaduto
2. La chiave segreta sia la stessa usata per generare il token
3. Il token sia nel formato corretto: `Bearer <token>`

## Licenza

Tutti i diritti riservati © 2025
