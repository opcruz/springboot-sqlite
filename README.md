# Ecommerce Demo using Spring Boot and SQLite

[![CI Build](https://github.com/opcruz/springboot-sqlite/actions/workflows/build-and-test.yml/badge.svg?branch=master)](https://github.com/opcruz/springboot-sqlite/actions/workflows/build-and-test.yml)

This is a simple demo of an ecommerce application built with Spring Boot and SQLite. The API provides endpoints for managing products, customers, and orders, following the principles of REST (Representational State Transfer).
## Features

- CRUD operations for products, customers, and orders
- Product search functionality
- Basic authentication and authorization

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- SQLite

## Getting Started

### Prerequisites

- Java 17 or above
- Maven

### Installation

1. Clone the repository:

```shell
git clone https://github.com/opcruz/springboot-sqlite.git
```

2. Navigate to the project directory:

```shell
cd springboot-sqlite
```

3. Build the project:

```shell
mvn clean package
```

4. Run the application:

```shell
mvn spring-boot:run
```

5. Access the API:

- [API testing](http://localhost:8080/ecommerce/api/v1/swagger-ui/index.html)


## GraalVM Native Image

```shell
mvn -Pnative native:compile
```
