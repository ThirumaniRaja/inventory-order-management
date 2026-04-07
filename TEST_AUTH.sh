#!/bin/bash

# Authentication & Authorization Testing Guide
# This script demonstrates how to test the JWT authentication system

BASE_URL="http://localhost:9000"
echo "============================================"
echo "JWT Authentication Testing Guide"
echo "============================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test 1: Register a USER
echo -e "${BLUE}TEST 1: Register a USER${NC}"
echo "Command:"
echo "curl -X POST $BASE_URL/api/auth/register \\"
echo '  -H "Content-Type: application/json" \'
echo '  -d '"'"'{
    "username": "john_user",
    "password": "SecurePass123!",
    "email": "john@example.com",
    "role": "USER"
  }'"'"''
echo ""
echo "Expected Response (201 Created):"
echo "{
  \"token\": \"eyJhbGciOiJIUzUxMiJ9...\",
  \"userId\": 1,
  \"username\": \"john_user\",
  \"email\": \"john@example.com\",
  \"role\": \"USER\"
}"
echo ""
echo -e "${YELLOW}Save the token for next requests${NC}"
echo ""

# Test 2: Register an ADMIN
echo -e "${BLUE}TEST 2: Register an ADMIN${NC}"
echo "Command:"
echo "curl -X POST $BASE_URL/api/auth/register \\"
echo '  -H "Content-Type: application/json" \'
echo '  -d '"'"'{
    "username": "admin_user",
    "password": "AdminPass123!",
    "email": "admin@example.com",
    "role": "ADMIN"
  }'"'"''
echo ""
echo "Expected Response (201 Created):"
echo "{
  \"token\": \"eyJhbGciOiJIUzUxMiJ9...\",
  \"userId\": 2,
  \"username\": \"admin_user\",
  \"email\": \"admin@example.com\",
  \"role\": \"ADMIN\"
}"
echo ""

# Test 3: Login with USER credentials
echo -e "${BLUE}TEST 3: Login with USER credentials${NC}"
echo "Command:"
echo "curl -X POST $BASE_URL/api/auth/login \\"
echo '  -H "Content-Type: application/json" \'
echo '  -d '"'"'{
    "username": "john_user",
    "password": "SecurePass123!"
  }'"'"''
echo ""
echo "Expected Response (200 OK):"
echo "{
  \"token\": \"eyJhbGciOiJIUzUxMiJ9...\",
  \"userId\": 1,
  \"username\": \"john_user\",
  \"email\": \"john@example.com\",
  \"role\": \"USER\"
}"
echo ""

# Test 4: Login with ADMIN credentials
echo -e "${BLUE}TEST 4: Login with ADMIN credentials${NC}"
echo "Command:"
echo "curl -X POST $BASE_URL/api/auth/login \\"
echo '  -H "Content-Type: application/json" \'
echo '  -d '"'"'{
    "username": "admin_user",
    "password": "AdminPass123!"
  }'"'"''
echo ""
echo "Expected Response (200 OK):"
echo "{
  \"token\": \"eyJhbGciOiJIUzUxMiJ9...\",
  \"userId\": 2,
  \"username\": \"admin_user\",
  \"email\": \"admin@example.com\",
  \"role\": \"ADMIN\"
}"
echo ""

# Test 5: USER accessing their orders
echo -e "${BLUE}TEST 5: USER accessing their orders (ALLOWED)${NC}"
echo "Command:"
echo "curl -X GET $BASE_URL/api/orders \\"
echo '  -H "Authorization: Bearer <USER_TOKEN>"'
echo ""
echo "Expected Response (200 OK):"
echo "Your orders (User only)"
echo ""

# Test 6: USER trying to access products (should fail)
echo -e "${BLUE}TEST 6: USER trying to access products (DENIED)${NC}"
echo "Command:"
echo "curl -X GET $BASE_URL/api/products \\"
echo '  -H "Authorization: Bearer <USER_TOKEN>"'
echo ""
echo "Expected Response (403 Forbidden):"
echo "Access Denied"
echo ""

# Test 7: ADMIN accessing products
echo -e "${BLUE}TEST 7: ADMIN accessing products (ALLOWED)${NC}"
echo "Command:"
echo "curl -X GET $BASE_URL/api/products \\"
echo '  -H "Authorization: Bearer <ADMIN_TOKEN>"'
echo ""
echo "Expected Response (200 OK):"
echo "List of all products (Admin only)"
echo ""

# Test 8: ADMIN creating a product
echo -e "${BLUE}TEST 8: ADMIN creating a product (ALLOWED)${NC}"
echo "Command:"
echo "curl -X POST $BASE_URL/api/products \\"
echo '  -H "Content-Type: application/json" \'
echo '  -H "Authorization: Bearer <ADMIN_TOKEN>" \'
echo '  -d '"'"'{
    "name": "Laptop",
    "price": 999.99,
    "categoryId": 1
  }'"'"''
echo ""
echo "Expected Response (201 Created):"
echo "Product created (Admin only)"
echo ""

# Test 9: ADMIN accessing inventory
echo -e "${BLUE}TEST 9: ADMIN accessing inventory (ALLOWED)${NC}"
echo "Command:"
echo "curl -X GET $BASE_URL/api/inventory \\"
echo '  -H "Authorization: Bearer <ADMIN_TOKEN>"'
echo ""
echo "Expected Response (200 OK):"
echo "Current inventory status (Admin only)"
echo ""

# Test 10: No token provided
echo -e "${BLUE}TEST 10: Accessing protected endpoint without token (DENIED)${NC}"
echo "Command:"
echo "curl -X GET $BASE_URL/api/orders"
echo ""
echo "Expected Response (403 Forbidden):"
echo "Access Denied"
echo ""

# Test 11: Invalid token
echo -e "${BLUE}TEST 11: Using invalid token (DENIED)${NC}"
echo "Command:"
echo "curl -X GET $BASE_URL/api/orders \\"
echo '  -H "Authorization: Bearer invalid.token.here"'
echo ""
echo "Expected Response (403 Forbidden):"
echo "Access Denied"
echo ""

# Test 12: Duplicate username
echo -e "${BLUE}TEST 12: Registering with duplicate username (DENIED)${NC}"
echo "Command:"
echo "curl -X POST $BASE_URL/api/auth/register \\"
echo '  -H "Content-Type: application/json" \'
echo '  -d '"'"'{
    "username": "john_user",
    "password": "AnotherPass123!",
    "email": "another@example.com",
    "role": "USER"
  }'"'"''
echo ""
echo "Expected Response (400 Bad Request):"
echo "(Empty or error message)"
echo ""

# Test 13: Wrong password
echo -e "${BLUE}TEST 13: Login with wrong password (DENIED)${NC}"
echo "Command:"
echo "curl -X POST $BASE_URL/api/auth/login \\"
echo '  -H "Content-Type: application/json" \'
echo '  -d '"'"'{
    "username": "john_user",
    "password": "WrongPassword"
  }'"'"''
echo ""
echo "Expected Response (401 Unauthorized):"
echo "(Empty response)"
echo ""

echo ""
echo "============================================"
echo "Summary of Access Rules"
echo "============================================"
echo ""
echo -e "${GREEN}✓ PUBLIC ENDPOINTS${NC} (No token required)"
echo "  - POST /api/auth/register"
echo "  - POST /api/auth/login"
echo ""
echo -e "${GREEN}✓ USER ENDPOINTS${NC} (Requires ROLE_USER)"
echo "  - GET  /api/orders"
echo "  - POST /api/orders"
echo "  - GET  /api/orders/{orderId}"
echo "  - DELETE /api/orders/{orderId}"
echo "  - GET  /api/orders/history"
echo ""
echo -e "${GREEN}✓ ADMIN ENDPOINTS${NC} (Requires ROLE_ADMIN)"
echo "  - GET    /api/products"
echo "  - POST   /api/products"
echo "  - PUT    /api/products/{id}"
echo "  - DELETE /api/products/{id}"
echo ""
echo "  - GET    /api/categories"
echo "  - POST   /api/categories"
echo "  - PUT    /api/categories/{id}"
echo "  - DELETE /api/categories/{id}"
echo ""
echo "  - GET    /api/inventory"
echo "  - PUT    /api/inventory/{productId}"
echo "  - GET    /api/inventory/{productId}/stock"
echo ""

echo ""
echo "============================================"
echo "Password Encryption Details"
echo "============================================"
echo ""
echo "Algorithm: BCrypt"
echo "Salt Rounds: 10 (default)"
echo "Strength: Very High"
echo ""
echo "Example:"
echo "  Plain text: SecurePass123!"
echo "  Hashed: \$2a\$10\$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWDeaB6qxJ5G/DK2"
echo ""

echo ""
echo "============================================"
echo "Token Structure"
echo "============================================"
echo ""
echo "Header (base64 decoded):"
echo "{
  \"alg\": \"HS512\",
  \"typ\": \"JWT\"
}"
echo ""
echo "Payload (base64 decoded):"
echo "{
  \"sub\": \"1\",
  \"role\": \"USER\",
  \"iat\": 1712500000,
  \"exp\": 1712503600
}"
echo ""
echo "How to decode and inspect:"
echo "  1. Go to https://jwt.io"
echo "  2. Paste your token in the Encoded section"
echo "  3. View the decoded header and payload"
echo ""

echo ""
echo -e "${GREEN}Setup Instructions:${NC}"
echo "1. Start the application: mvn spring-boot:run"
echo "2. Database will auto-create tables using DDL"
echo "3. Use 'curl' or Postman to test endpoints"
echo "4. Copy tokens from responses and use in subsequent requests"
echo ""

