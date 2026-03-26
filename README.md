# FreshCart Marketplace

An online grocery marketplace built with **Spring Boot 2.6**, **Hibernate 5**, **Spring Security**, and **JSP/JSTL** views backed by **MySQL**.

## Features

- **Customer-facing storefront** — browse products, view catalog, register/login
- **Admin dashboard** — manage categories, products, and view registered customers
- **Role-based security** — separate login flows for customers (`ROLE_NORMAL`) and admins (`ROLE_ADMIN`)
- **Shopping cart** infrastructure (entity model in place)

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 2.6.4 |
| ORM | Hibernate 5 (manual session factory) |
| Security | Spring Security (BCrypt, dual filter chains) |
| Database | MySQL 8.x |
| Views | JSP + JSTL + Bootstrap 4 |
| Build | Maven |

## Project Structure

```
src/main/java/com/freshcart/marketplace/
├── FreshCartApplication.java              # Entry point
├── domain/entity/                         # JPA entities
│   ├── Customer.java
│   ├── ProductGroup.java
│   ├── Merchandise.java
│   ├── ShoppingCart.java
│   ├── CartLineItem.java
│   └── CartLineItemKey.java
├── infrastructure/
│   ├── config/
│   │   ├── DatabaseConfig.java            # Hibernate & DataSource setup
│   │   └── WebSecurityConfig.java         # Spring Security dual-chain config
│   ├── persistence/                       # DAO / data access layer
│   │   ├── CustomerPersistence.java
│   │   ├── ProductPersistence.java
│   │   ├── CategoryPersistence.java
│   │   ├── ShoppingCartPersistence.java
│   │   └── CartItemPersistence.java
│   └── repository/
│       └── CartLineItemRepository.java    # Spring Data JPA repo
├── application/service/                   # Business logic
│   ├── CustomerAccountService.java
│   ├── ProductCatalogService.java
│   ├── CategoryManagementService.java
│   └── ShoppingCartService.java
└── web/controller/                        # MVC controllers
    ├── AdminPanelController.java
    ├── StorefrontController.java
    └── AccessDeniedController.java
```

## Getting Started

### Prerequisites
- Java 11+
- MySQL 8.x running on `localhost:3306`
- Maven 3.6+

### Database Setup

```bash
mysql -u root -p < basedata.sql
```

### Run

```bash
cd JtProject
./mvnw spring-boot:run
```

The app starts on **http://localhost:8080**

### Default Accounts

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `123` |
| Customer | `lisa` | `765` |

## Endpoints

| URL | Role | Description |
|-----|------|-------------|
| `/` | USER | Storefront homepage |
| `/login` | Public | Customer login |
| `/register` | Public | New customer registration |
| `/user/products` | USER | Browse product catalog |
| `/profileDisplay` | USER | View/edit profile |
| `/admin/login` | Public | Admin login |
| `/admin/` | ADMIN | Admin dashboard |
| `/admin/categories` | ADMIN | Manage categories |
| `/admin/products` | ADMIN | Manage products |
| `/admin/customers` | ADMIN | View customer list |

## License

This project is provided as-is for educational purposes.
