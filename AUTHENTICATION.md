# JWT Authentication & Authorization Implementation

## Overview
This document describes the JWT-based authentication system with BCrypt password encryption and role-based access control (RBAC) implemented in the Inventory Order Management application.

## Architecture

### Components

1. **User Entity** (`com.guvi.inventory.model.User`)
   - Stores user credentials with BCrypt-encrypted passwords
   - Roles: ADMIN or USER
   - Active status for account management

2. **JWT Configuration** (`com.guvi.inventory.config.JwtUtil`)
   - Generates JWT tokens with user ID and role claims
   - Validates token signatures and expiration
   - Extracts user information from tokens
   - Configurable expiration time (default: 60 minutes)

3. **Security Filter** (`com.guvi.inventory.config.JwtFilter`)
   - Intercepts HTTP requests
   - Extracts JWT token from Authorization header
   - Validates token and sets security context
   - Applies role-based authorities

4. **Security Configuration** (`com.guvi.inventory.config.SecurityConfig`)
   - Configures Spring Security for stateless sessions
   - Defines access rules for different API endpoints
   - Registers JWT filter
   - Configures BCrypt password encoder

5. **Authentication Service** (`com.guvi.inventory.services.AuthService`)
   - Handles user login and registration
   - Validates credentials with BCrypt comparison
   - Generates JWT tokens upon successful authentication

6. **Auth Controller** (`com.guvi.inventory.controller.AuthController`)
   - Public endpoints for login and registration
   - Returns JWT token and user information

## API Endpoints

### Authentication Endpoints (Public)

#### 1. Register User
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePassword123",
  "email": "john@example.com",
  "role": "USER"
}

Response (201 Created):
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER"
}
```

#### 2. Login User
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePassword123"
}

Response (200 OK):
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER"
}
```

### Protected Endpoints

#### Admin-Only Endpoints
- `GET/POST/PUT/DELETE /api/products/**` - Product management (ROLE_ADMIN)
- `GET/POST/PUT/DELETE /api/categories/**` - Category management (ROLE_ADMIN)
- `GET/POST/PUT/DELETE /api/inventory/**` - Inventory management (ROLE_ADMIN)

#### User Endpoints
- `GET/POST /api/orders/**` - Order placement and viewing (ROLE_USER)

## Access Rules

| Role | Permissions |
|------|-------------|
| ADMIN | Manage products, categories, and inventory |
| USER | Place and view orders |

## Security Features

### 1. Password Encryption
- Uses **BCrypt** hashing algorithm
- Salt rounds: 10 (default Spring Security)
- Passwords never stored in plaintext
- One-way hashing - passwords cannot be decrypted

### 2. JWT Token Structure
```
Header: {
  "alg": "HS512",
  "typ": "JWT"
}

Payload: {
  "sub": "1",           // userId
  "role": "USER",
  "iat": 1234567890,    // issued at
  "exp": 1234571490     // expiration
}

Signature: HMACSHA512(header.payload, secret_key)
```

### 3. Token Validation
- Validates HMAC signature using secret key
- Checks token expiration time
- Extracts role and userId on each request

### 4. Stateless Authentication
- No session cookies required
- Each request includes token in Authorization header
- Reduces server memory overhead

## Configuration

### application.properties
```properties
# JWT Settings
app.jwt.expirationMinutes=60
app.jwt.secret=i-am-a-super-secret-i-am-a-super-secret-i-am-a-super-secret
```

### Update Secret Key
For production:
1. Change `app.jwt.secret` to a strong, randomly generated string (minimum 256 bits for HS512)
2. Store in environment variables or secure vault, not in source code
3. Use a key management service

## Request Flow

### With JWT Token
```
1. Client sends request with JWT in Authorization header
   GET /api/orders
   Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...

2. JwtFilter intercepts request
   - Extracts token from header
   - Validates signature and expiration
   - Creates authentication object with userId and role

3. SecurityConfig checks authorization rules
   - Verifies user has required role (ROLE_USER)
   - Allows request if role matches

4. Controller processes request
   - Executes business logic
   - Returns response

5. Client receives response
```

## Usage Examples

### Example 1: Register as USER and Place Order

```bash
# 1. Register user
curl -X POST http://localhost:9000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "customer1",
    "password": "Pass123!@#",
    "email": "customer1@example.com",
    "role": "USER"
  }'

# Response:
# {
#   "token": "eyJhbGciOiJIUzUxMiJ9...",
#   "userId": 1,
#   "username": "customer1",
#   "role": "USER"
# }

# 2. Use token to place order
curl -X POST http://localhost:9000/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -d '{
    "productId": 5,
    "quantity": 10
  }'
```

### Example 2: Register as ADMIN and Manage Products

```bash
# 1. Register admin
curl -X POST http://localhost:9000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin1",
    "password": "AdminPass123!@#",
    "email": "admin@example.com",
    "role": "ADMIN"
  }'

# Response:
# {
#   "token": "eyJhbGciOiJIUzUxMiJ9...",
#   "userId": 2,
#   "username": "admin1",
#   "role": "ADMIN"
# }

# 2. Use token to create product
curl -X POST http://localhost:9000/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -d '{
    "name": "Laptop",
    "price": 999.99,
    "categoryId": 1
  }'
```

### Example 3: Unauthorized Access

```bash
# Try to access admin endpoint as USER
curl -X GET http://localhost:9000/api/products \
  -H "Authorization: Bearer <USER_TOKEN>"

# Response: 403 Forbidden
```

## Error Handling

| Error | Status | Reason |
|-------|--------|--------|
| Invalid credentials | 401 | Wrong username/password |
| User not found | 401 | User doesn't exist |
| Invalid token | 403 | Token signature invalid or expired |
| Insufficient permissions | 403 | User role doesn't have access |
| Username exists | 400 | Duplicate username during registration |
| Email exists | 400 | Duplicate email during registration |

## Security Best Practices

### 1. Secret Key Management
✅ DO:
- Use environment variables for secret key
- Rotate keys periodically
- Use strong random strings (256+ bits)
- Store securely in vault/KMS

❌ DON'T:
- Hardcode secrets in source code
- Use weak/simple strings
- Share secrets publicly
- Commit secrets to version control

### 2. Token Handling
✅ DO:
- Send token in Authorization header
- Use HTTPS/TLS for all requests
- Implement token refresh mechanism
- Set reasonable expiration times

❌ DON'T:
- Send tokens in URL parameters
- Store tokens in local storage (web)
- Use HTTP for authentication
- Set very long expiration times

### 3. Password Security
✅ DO:
- Enforce minimum password length (8+ characters)
- Require mixed case, numbers, special characters
- Hash with BCrypt or similar
- Implement rate limiting on login

❌ DON'T:
- Store passwords in plaintext
- Use simple hashing (MD5, SHA1)
- Allow weak passwords
- Send passwords in logs/responses

## Future Enhancements

1. **Token Refresh**
   - Implement refresh token mechanism
   - Shorter access token expiration
   - Secure refresh token storage

2. **Two-Factor Authentication**
   - Email/SMS verification
   - TOTP (Time-based One-Time Password)

3. **Rate Limiting**
   - Limit login attempts per IP
   - Prevent brute force attacks

4. **Audit Logging**
   - Log all authentication events
   - Monitor suspicious activities

5. **OAuth2/OpenID Connect**
   - External identity provider support
   - Social login integration

## Troubleshooting

### Issue: "Invalid token" error on requests
**Solution**: 
- Ensure token is included in Authorization header with "Bearer " prefix
- Check token hasn't expired (default: 60 minutes)
- Verify secret key matches

### Issue: "403 Forbidden" on authorized endpoint
**Solution**:
- Verify user has required role
- Check role name matches endpoint requirement (ROLE_ADMIN vs ROLE_USER)
- Ensure token contains correct role claim

### Issue: "401 Unauthorized" on login
**Solution**:
- Verify username and password are correct
- Check user account is active
- Ensure user exists in database

## Database Schema

### users table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  role ENUM('ADMIN', 'USER') NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## Dependencies

The following dependencies are required:

```xml
<!-- Spring Security -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT (JJWT) -->
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.13.0</version>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-impl</artifactId>
  <version>0.13.0</version>
</dependency>

<!-- Lombok -->
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <optional>true</optional>
</dependency>

<!-- Spring Data JPA -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

## Summary

This authentication system provides:
- ✅ Secure password storage with BCrypt
- ✅ JWT-based stateless authentication
- ✅ Role-based access control (ADMIN/USER)
- ✅ Easy integration with Spring Security
- ✅ Configurable token expiration
- ✅ Automatic role extraction and authorization

