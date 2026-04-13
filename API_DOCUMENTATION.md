# Inventory Order Management System - Complete API Documentation

## Table of Contents
1. [Authentication & Authorization](#authentication--authorization)
2. [Category Management](#category-management)
3. [Product Management](#product-management)
4. [Order Management](#order-management)
5. [Filtering & Searching](#filtering--searching)
6. [Data Models](#data-models)
7. [Error Handling](#error-handling)
8. [Testing Guide](#testing-guide)

---

## Authentication & Authorization

### Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_user",
  "password": "SecurePass123!",
  "email": "john@example.com",
  "role": "USER"
}

Response: 201 Created
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "username": "john_user",
  "email": "john@example.com",
  "role": "USER"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_user",
  "password": "SecurePass123!"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "username": "john_user",
  "email": "john@example.com",
  "role": "USER"
}
```

### Authorization Rules

| Endpoint Pattern | Required Role | Permissions |
|-----------------|---------------|-------------|
| `/api/auth/**` | None (Public) | Register and login |
| `/api/categories/**` | ADMIN | Create, read, update, delete categories |
| `/api/products/**` | ADMIN | Create, read, update, activate/deactivate products |
| `/api/inventory/**` | ADMIN | View stock and low-stock products |
| `/api/orders/**` | USER | Create, read, cancel orders |

---

## Category Management

### Endpoints

#### Get All Categories
```http
GET /api/categories
Authorization: Bearer <ADMIN_TOKEN>

Response: 200 OK
[
  {
    "id": 1,
    "name": "Electronics",
    "description": "Electronic devices and gadgets",
    "active": true
  },
  {
    "id": 2,
    "name": "Clothing",
    "description": "Apparel and accessories",
    "active": true
  }
]
```

#### Create Category
```http
POST /api/categories
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "name": "Books",
  "description": "Physical and digital books"
}

Response: 201 Created
{
  "id": 3,
  "name": "Books",
  "description": "Physical and digital books",
  "active": true
}
```

#### Update Category
```http
PUT /api/categories/{categoryId}
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "name": "Electronics & Gadgets",
  "description": "Updated description"
}

Response: 200 OK
{
  "id": 1,
  "name": "Electronics & Gadgets",
  "description": "Updated description",
  "active": true
}
```

#### Delete Category
```http
DELETE /api/categories/{categoryId}
Authorization: Bearer <ADMIN_TOKEN>

Response: 204 No Content
```

---

## Product Management

### Endpoints

#### Get All Active Products (Paginated)
```http
GET /api/products?page=0&size=10&sort=id,desc
Authorization: Bearer <ADMIN_TOKEN>

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "name": "Laptop",
      "description": "High-performance laptop",
      "price": 999.99,
      "stockQuantity": 50,
      "categoryId": 1,
      "categoryName": "Electronics",
      "status": "ACTIVE"
    }
  ],
  "pageable": {...},
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0,
  "numberOfElements": 10
}
```

#### Create Product
```http
POST /api/products
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stockQuantity": 50,
  "categoryId": 1
}

Response: 201 Created
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stockQuantity": 50,
  "categoryId": 1,
  "categoryName": "Electronics",
  "status": "ACTIVE"
}
```

#### Update Product
```http
PUT /api/products/{productId}
Authorization: Bearer <ADMIN_TOKEN>
Content-Type: application/json

{
  "name": "Laptop Pro",
  "description": "Professional laptop",
  "price": 1299.99,
  "stockQuantity": 30,
  "categoryId": 1
}

Response: 200 OK
{
  "id": 1,
  "name": "Laptop Pro",
  "description": "Professional laptop",
  "price": 1299.99,
  "stockQuantity": 30,
  "categoryId": 1,
  "categoryName": "Electronics",
  "status": "ACTIVE"
}
```

#### Deactivate Product
```http
PATCH /api/products/{productId}/deactivate
Authorization: Bearer <ADMIN_TOKEN>

Response: 204 No Content
```

#### Activate Product
```http
PATCH /api/products/{productId}/activate
Authorization: Bearer <ADMIN_TOKEN>

Response: 204 No Content
```

### Filtering & Searching

#### Get Products by Category
```http
GET /api/products/category/{categoryId}?page=0&size=10
Authorization: Bearer <ADMIN_TOKEN>

Response: 200 OK
{
  "content": [...],
  "totalElements": 25,
  "totalPages": 3
}
```

#### Search Products by Name
```http
GET /api/products/search?name=laptop&page=0&size=10
Authorization: Bearer <ADMIN_TOKEN>

Response: 200 OK
{
  "content": [...],
  "totalElements": 5,
  "totalPages": 1
}
```

#### Sort Products by Price (Ascending)
```http
GET /api/products/sort/price-asc?page=0&size=10
Authorization: Bearer <ADMIN_TOKEN>

Response: 200 OK
{
  "content": [
    {
      "id": 5,
      "name": "Book",
      "price": 15.99,
      ...
    },
    {
      "id": 1,
      "name": "Laptop",
      "price": 999.99,
      ...
    }
  ]
}
```

#### Sort Products by Price (Descending)
```http
GET /api/products/sort/price-desc?page=0&size=10
Authorization: Bearer <ADMIN_TOKEN>

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "name": "Laptop",
      "price": 999.99,
      ...
    },
    {
      "id": 5,
      "name": "Book",
      "price": 15.99,
      ...
    }
  ]
}
```

#### View Low Stock Products
```http
GET /api/products/low-stock?threshold=10
Authorization: Bearer <ADMIN_TOKEN>

Response: 200 OK
[
  {
    "id": 2,
    "name": "Mouse",
    "stockQuantity": 5,
    ...
  },
  {
    "id": 3,
    "name": "Keyboard",
    "stockQuantity": 8,
    ...
  }
]
```

---

## Order Management

### Endpoints

#### Get All User Orders (Paginated)
```http
GET /api/orders?page=0&size=10&sort=createdAt,desc
Authorization: Bearer <USER_TOKEN>

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "userId": 1,
      "username": "john_user",
      "status": "CONFIRMED",
      "totalAmount": 1199.97,
      "items": [
        {
          "id": 1,
          "productId": 1,
          "productName": "Laptop",
          "quantity": 1,
          "unitPrice": 999.99,
          "subtotal": 999.99
        },
        {
          "id": 2,
          "productId": 5,
          "productName": "Mouse",
          "quantity": 2,
          "unitPrice": 99.99,
          "subtotal": 199.98
        }
      ],
      "createdAt": "2026-04-12T10:30:00",
      "updatedAt": "2026-04-12T10:30:00"
    }
  ],
  "totalElements": 5,
  "totalPages": 1
}
```

#### Place Order
```http
POST /api/orders
Authorization: Bearer <USER_TOKEN>
Content-Type: application/json

{
  "items": [
    {
      "productId": 1,
      "quantity": 1
    },
    {
      "productId": 5,
      "quantity": 2
    }
  ]
}

Response: 201 Created
{
  "id": 1,
  "userId": 1,
  "username": "john_user",
  "status": "CONFIRMED",
  "totalAmount": 1199.97,
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "quantity": 1,
      "unitPrice": 999.99,
      "subtotal": 999.99
    },
    {
      "id": 2,
      "productId": 5,
      "productName": "Mouse",
      "quantity": 2,
      "unitPrice": 99.99,
      "subtotal": 199.98
    }
  ],
  "createdAt": "2026-04-12T10:30:00",
  "updatedAt": "2026-04-12T10:30:00"
}
```

#### Get Order Details
```http
GET /api/orders/{orderId}
Authorization: Bearer <USER_TOKEN>

Response: 200 OK
{
  "id": 1,
  "userId": 1,
  "username": "john_user",
  "status": "CONFIRMED",
  "totalAmount": 1199.97,
  "items": [...],
  "createdAt": "2026-04-12T10:30:00",
  "updatedAt": "2026-04-12T10:30:00"
}
```

#### Get Orders by Status
```http
GET /api/orders/status/CONFIRMED?page=0&size=10
Authorization: Bearer <USER_TOKEN>

Response: 200 OK
{
  "content": [...],
  "totalElements": 3,
  "totalPages": 1
}
```

#### Cancel Order
```http
DELETE /api/orders/{orderId}
Authorization: Bearer <USER_TOKEN>

Response: 204 No Content
```

---

## Data Models

### User
```json
{
  "id": 1,
  "username": "john_user",
  "password": "hashed_bcrypt_password",
  "email": "john@example.com",
  "role": "USER",
  "active": true
}
```

### Category
```json
{
  "id": 1,
  "name": "Electronics",
  "description": "Electronic devices",
  "active": true
}
```

### Product
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stockQuantity": 50,
  "category": {...},
  "status": "ACTIVE"
}
```

### Order
```json
{
  "id": 1,
  "user": {...},
  "status": "CONFIRMED",
  "totalAmount": 1199.97,
  "items": [...],
  "createdAt": "2026-04-12T10:30:00",
  "updatedAt": "2026-04-12T10:30:00"
}
```

### OrderItem
```json
{
  "id": 1,
  "order": {...},
  "product": {...},
  "quantity": 1,
  "unitPrice": 999.99,
  "subtotal": 999.99
}
```

---

## Error Handling

### Error Responses

#### 400 Bad Request
```json
Response when request data is invalid or business logic fails
```

#### 401 Unauthorized
```json
Response when credentials are invalid or token is missing/expired
```

#### 403 Forbidden
```json
Response when user lacks required role/permission
```

#### 404 Not Found
```json
Response when resource doesn't exist
```

### Common Error Scenarios

| Error | Status | Cause | Solution |
|-------|--------|-------|----------|
| Duplicate username | 400 | Username already exists | Use different username |
| Insufficient stock | 400 | Not enough product quantity | Reduce order quantity |
| Invalid token | 403 | Token signature invalid or expired | Login again to get new token |
| Category not found | 404 | Invalid category ID | Verify category ID exists |
| Product inactive | 400 | Cannot order inactive product | Order only active products |
| Unauthorized access | 401 | Invalid credentials | Check username/password |

---

## Testing Guide

### Prerequisites
- Application running on `http://localhost:9000`
- PostgreSQL database running
- curl or Postman installed

### Test Workflow

#### 1. Register Users
```bash
# Register as USER
curl -X POST http://localhost:9000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "customer1",
    "password": "Pass123!@#",
    "email": "customer1@example.com",
    "role": "USER"
  }'

# Save USER_TOKEN from response

# Register as ADMIN
curl -X POST http://localhost:9000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin1",
    "password": "AdminPass123!@#",
    "email": "admin1@example.com",
    "role": "ADMIN"
  }'

# Save ADMIN_TOKEN from response
```

#### 2. Create Categories (ADMIN)
```bash
curl -X POST http://localhost:9000/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices"
  }'

curl -X POST http://localhost:9000/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "Clothing",
    "description": "Apparel and fashion"
  }'
```

#### 3. Create Products (ADMIN)
```bash
curl -X POST http://localhost:9000/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stockQuantity": 50,
    "categoryId": 1
  }'

curl -X POST http://localhost:9000/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "name": "Mouse",
    "description": "Wireless mouse",
    "price": 29.99,
    "stockQuantity": 100,
    "categoryId": 1
  }'
```

#### 4. Place Order (USER)
```bash
curl -X POST http://localhost:9000/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 1
      },
      {
        "productId": 2,
        "quantity": 2
      }
    ]
  }'
```

#### 5. View Orders (USER)
```bash
curl -X GET "http://localhost:9000/api/orders?page=0&size=10" \
  -H "Authorization: Bearer $USER_TOKEN"
```

#### 6. View Low Stock (ADMIN)
```bash
curl -X GET "http://localhost:9000/api/inventory/low-stock?threshold=30" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

---

## Features Summary

### ✅ Implemented Features

1. **Authentication & Authorization**
   - JWT-based stateless authentication
   - BCrypt password encryption
   - Role-based access control (ADMIN/USER)
   - Secure API endpoints

2. **Category Management**
   - Create, read, update, delete categories
   - Soft delete (deactivate) categories
   - List all active categories

3. **Product Management**
   - Create, read, update products
   - Activate/deactivate products
   - Set price and stock quantity
   - Product status tracking (ACTIVE/INACTIVE)

4. **Inventory Management**
   - Track stock quantities
   - Low-stock alerts
   - Stock validation during order placement
   - Automatic stock reduction on order confirmation

5. **Order Management**
   - Place orders with multiple items
   - View order history
   - Cancel orders with stock restoration
   - Order status tracking (CREATED/CONFIRMED/CANCELLED)

6. **Filtering & Searching**
   - Filter products by category
   - Search products by name (case-insensitive)
   - Sort products by price (ascending/descending)
   - Pagination support for all list endpoints
   - View low-stock products

7. **Data Consistency**
   - Transactional order creation
   - Stock validation before order placement
   - Stock restoration on order cancellation
   - Prevent duplicate usernames/emails

---

## Configuration

### application.properties
```properties
# Server
server.port=9000

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/subscription_manager
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update

# JWT
app.jwt.expirationMinutes=60
app.jwt.secret=i-am-a-super-secret-i-am-a-super-secret-i-am-a-super-secret
```

### Dependencies
All required dependencies are in `pom.xml`:
- Spring Boot Web, Security, Data JPA
- JJWT (JWT library)
- BCrypt (Password encoding)
- Lombok (Annotations)
- PostgreSQL driver

---

## Next Steps

1. Implement email notifications for orders
2. Add payment integration
3. Implement order tracking
4. Add admin analytics dashboard
5. Implement wishlist feature for users
6. Add product reviews and ratings
7. Implement discount codes
8. Add order status webhooks

