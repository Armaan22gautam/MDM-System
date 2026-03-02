# Customer Data Unification Platform (Mini MDM)

A microservices-based Master Data Management (MDM) system designed to aggregate, deduplicate, and merge customer data from multiple sources to create a definitive single source of truth, often referred to as a "Golden Record."

## Architecture

The system is built using an event-driven and RESTful microservices architecture with a Java/Spring Boot backend and a React frontend. The application is entirely containerized using Docker and Docker Compose for easy local development and deployment.

### Microservices

*   **API Gateway (`api-gateway`)**: Built with Spring Cloud Gateway, this service acts as the single point of entry into the system. It handles routing to the downstream microservices and enforces JWT authentication.
*   **Authentication Service (`auth-service`)**: Manages user registration, login, and issues JWT tokens for secure access to the platform.
*   **Ingestion Service (`ingestion-service`)**: Responsible for receiving raw, potentially duplicate customer records from various external sources and securely persisting them.
*   **Deduplication Service (`deduplication-service`)**: Analyzes raw customer data using matching algorithms to identify potential duplicates and calculate match confidence scores.
*   **Golden Record Service (`golden-record-service`)**: Applies survivorship rules to merge duplicate raw records into a single, authoritative Golden Customer record. It also maintains an audit log of all changes.
*   **Frontend (`frontend`)**: A React-based Single Page Application (SPA) providing a modern user interface to view dashboards, ingest new records, and visualize the final Golden Records.

## Technology Stack

*   **Backend**: Java 17+, Spring Boot 3+, Spring Cloud (Gateway, OpenFeign), Spring Data JPA, Spring Security, JWT
*   **Database**: MySQL (Containerized)
*   **Frontend**: React (Vite), JavaScript, CSS
*   **Infrastructure**: Docker, Docker Compose, Maven

## Getting Started

### Prerequisites
*   Docker and Docker Compose
*   Java 17 (If running outside Docker)
*   Maven
*   Node.js (for frontend development)

### Running Locally with Docker

The easiest way to start the entire system (all microservices, frontend, and the MySQL database) is using Docker Compose.

1.  Clone the repository:
    ```bash
    git clone https://github.com/Armaan22gautam/MDM-System.git
    cd MDM-System
    ```

2.  Start the infrastructure and services:
    ```bash
    docker-compose up --build -d
    ```

3.  Access the application:
    *   **Frontend UI**: `http://localhost:5173`
    *   **API Gateway**: `http://localhost:8080`

### Building from Source

To build the Java microservices locally without Docker:

```bash
mvn clean install -DskipTests
```

To run the React frontend locally:
```bash
cd frontend
npm install
npm run dev
```
