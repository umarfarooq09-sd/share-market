# Post-Concall Announcement Drift

A live board tracking how Indian stocks (NSE/BSE) move in the hours and days after an earnings concall. Baselines are anchored at the announcement time and compared against real-time market activity.

## Stack

| Layer    | Technology             |
|----------|------------------------|
| Frontend | React 18 + Vite        |
| Backend  | Java 17 + Spring Boot 3.2 |
| Database | MySQL 8.x              |
| Auth     | JWT (jjwt 0.11.5)      |

## Features

- JWT-based login (invitation-only, no public sign-up)
- Live drift board showing price movement at 1H / 4H / 1D / 2D / 5D intervals after each concall
- Filter by exchange (NSE/BSE) and concall date range
- CSV export of the filtered board
- Auto-refresh every 30 seconds
- Dark professional financial UI

## Project Structure

```
share-market/
├── backend/            # Spring Boot API
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/concalldrift/
│       │   ├── config/         SecurityConfig, DataInitializer
│       │   ├── controller/     AuthController, DriftController, StockController
│       │   ├── dto/            LoginRequest/Response, DriftBoardRow, ApiResponse
│       │   ├── exception/      GlobalExceptionHandler
│       │   ├── model/          User, Stock, ConcallEvent, PriceSnapshot
│       │   ├── repository/     JPA repositories
│       │   ├── security/       JwtTokenProvider, JwtAuthFilter, UserDetailsServiceImpl
│       │   └── service/        AuthService, DriftService, StockService
│       └── resources/
│           ├── application.properties
│           └── data.sql        (seed stocks + concall events + price snapshots)
├── frontend/           # React + Vite app
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── App.jsx
│       ├── main.jsx
│       ├── components/     DriftBoard, DriftCell, FilterBar
│       ├── pages/          LoginPage, DashboardPage
│       ├── services/       api.js, auth.js
│       └── styles/         index.css
└── database/
    └── schema.sql      (standalone DDL if needed)
```

## Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8.x running on localhost:3306

### 1. Database

The application auto-creates the `concall_drift` database on first run (via `createDatabaseIfNotExist=true` in the connection URL). To create the schema manually:

```sql
mysql -u root -p < database/schema.sql
```

### 2. Backend

Update DB credentials in `backend/src/main/resources/application.properties` if needed:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

Run the Spring Boot app:

```bash
cd backend
mvn spring-boot:run
```

The API starts on **http://localhost:8080/api**

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

The React app starts on **http://localhost:3000**

## Default Credentials

| Username | Password                | Role  |
|----------|-------------------------|-------|
| DEMO1    | Tiger-Ocean-Marble-61   | USER  |
| admin    | Admin@123               | ADMIN |

## API Endpoints

| Method | Endpoint                | Auth | Description                      |
|--------|-------------------------|------|----------------------------------|
| POST   | /api/auth/login         | No   | Login, returns JWT token         |
| GET    | /api/auth/verify        | Yes  | Validate token                   |
| GET    | /api/drift/board        | Yes  | Live drift board (filterable)    |
| GET    | /api/drift/export/csv   | Yes  | CSV download of board data       |
| GET    | /api/stocks             | Yes  | List all tracked stocks          |

### Query Parameters for `/api/drift/board`

| Param     | Example       | Description           |
|-----------|---------------|-----------------------|
| exchange  | NSE           | Filter by NSE or BSE  |
| fromDate  | 2025-04-01    | Concall date from     |
| toDate    | 2025-05-31    | Concall date to       |
