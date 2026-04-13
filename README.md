# Inventory Order Management System

A comprehensive Java Spring Boot application for managing inventory, products, categories, and customer orders with role-based authentication and authorization.

## 📋 Features

### 1. Authentication & Authorization ✅
- **JWT-based Authentication**: Stateless token-based authentication
- **BCrypt Password Encryption**: Secure password hashing with salt
- **Role-Based Access Control**:
  - `ADMIN`: Manage products, categories, and inventory
  - `USER`: Place and view orders
- **Secure API Endpoints**: All endpoints protected by role-based authorization

### 2. Category Management (Admin) ✅
- Create categories
- View all categories
- Update category details
- Soft delete (deactivate) categories
- Query active categories only

### 3. Product & Inventory Management (Admin) ✅
- Create products with:
  - Name, description, price
  - Stock quantity tracking
  - Category assignment
  - Status (ACTIVE/INACTIVE)
- Update product details
- Activate/Deactivate products
- Real-time stock management
- Low-stock product alerts

### 4. Order Management (User) ✅
- Browse available products
- Place orders with multiple items
- Automatic stock validation
- Automatic stock reduction on order confirmation
- View order history
- Cancel orders with stock restoration
- Order status tracking:
  - `CREATED`: Order initialized
  - `CONFIRMED`: Order placed and stock reduced
  - `CANCELLED`: Order cancelled and stock restored

### 5. Filtering & Custom Queries ✅
- **Search**: Search products by name (case-insensitive)
- **Filter**: Filter products by category
- **Sort**: Sort products by price (ascending/descending)
- **Pagination**: All list endpoints support pagination
- **Stock Alerts**: View products with low stock levels

## 🏗️ Architecture

### Technology Stack
- **Framework**: Spring Boot 4.0.1
- **Language**: Java 17
- **Database**: PostgreSQL
- **Authentication**: JWT (JJWT)
- **Password Encoding**: BCrypt
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **Annotations**: Lombok

### Project Structure
```
inventory-order-management/
├── src/main/java/com/guvi/inventory/
│   ├── config/              # Security & JWT configuration
│   ├── controller/          # REST controllers
│   ├── DTO/                 # Data Transfer Objects
│   ├── exception/           # Custom exceptions
│   ├── model/               # JPA entities
│   ├── repository/          # Database repositories
│   ├── services/            # Business logic
│   └── InventoryOrderManagement.java  # Main class
├── src/main/resources/
│   └── application.properties
├── API_DOCUMENTATION.md     # Complete API documentation
├── AUTHENTICATION.md        # Auth system details
└── pom.xml                  # Maven dependencies
```

## 📊 Data Models

### User
```
- id (Long)
- username (String, unique)
- password (String, hashed)
- email (String, unique)
- role (ADMIN / USER)
- active (Boolean)
```

### Category
```
- id (Long)
- name (String, unique)
- description (String)
- active (Boolean)
```

### Product
```
- id (Long)
- name (String)
- description (String)
- price (BigDecimal)
- stockQuantity (Long)
- category (Category)
- status (ACTIVE / INACTIVE)
```

### Order
```
- id (Long)
- user (User)
- status (CREATED / CONFIRMED / CANCELLED)
- totalAmount (BigDecimal)
- items (List<OrderItem>)
- createdAt (LocalDateTime)
- updatedAt (LocalDateTime)
```

### OrderItem
```
- id (Long)
- order (Order)
- product (Product)
- quantity (Long)
- unitPrice (BigDecimal)
- subtotal (BigDecimal)
```

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Git

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd inventory-order-management
```

2. **Configure database**
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/subscription_manager
spring.datasource.username=postgres
spring.datasource.password=admin
```

3. **Build the project**
```bash
mvn clean install
```

4. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:9000`

## 📚 API Documentation

### Quick Start

#### 1. Register User
```bash
curl -X POST http://localhost:9000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "Pass123!",
    "email": "user1@example.com",
    "role": "USER"
  }'
```

#### 2. Login
```bash
curl -X POST http://localhost:9000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "Pass123!"
  }'
```

#### 3. Create Category (Admin Only)
```bash
curl -X POST http://localhost:9000/api/categories \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices"
  }'
```

#### 4. Create Product (Admin Only)
```bash
curl -X POST http://localhost:9000/api/products \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stockQuantity": 50,
    "categoryId": 1
  }'
```

#### 5. Place Order (User Only)
```bash
curl -X POST http://localhost:9000/api/orders \
  -H "Authorization: Bearer <USER_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 1
      }
    ]
  }'
```

### Complete API Endpoints

**Authentication**
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

**Categories (Admin)**
- `GET /api/categories` - List all categories
- `POST /api/categories` - Create category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

**Products (Admin)**
- `GET /api/products` - List all products (paginated)
- `POST /api/products` - Create product
- `PUT /api/products/{id}` - Update product
- `PATCH /api/products/{id}/activate` - Activate product
- `PATCH /api/products/{id}/deactivate` - Deactivate product
- `GET /api/products/category/{categoryId}` - Get products by category
- `GET /api/products/search?name=...` - Search products
- `GET /api/products/sort/price-asc` - Sort by price ascending
- `GET /api/products/sort/price-desc` - Sort by price descending
- `GET /api/products/low-stock?threshold=...` - Low stock products

**Inventory (Admin)**
- `GET /api/inventory/{productId}/stock` - Get product stock
- `GET /api/inventory/low-stock?threshold=...` - Get low stock products

**Orders (User)**
- `GET /api/orders` - Get user's orders (paginated)
- `POST /api/orders` - Place new order
- `GET /api/orders/{orderId}` - Get order details
- `DELETE /api/orders/{orderId}` - Cancel order
- `GET /api/orders/status/{status}` - Get orders by status

For detailed API documentation, see [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

## 🔐 Security Features

### Authentication
- **JWT Tokens**: Configurable expiration (default: 60 minutes)
- **Token Validation**: Signature and expiration verification
- **Stateless**: No session storage required

### Authorization
- **Role-Based Access Control**: ADMIN and USER roles
- **@PreAuthorize Annotations**: Method-level security
- **Protected Endpoints**: All non-auth endpoints require valid token

### Password Security
- **BCrypt Hashing**: Industry-standard password hashing
- **Salt Rounds**: 10 (default Spring Security)
- **No Plaintext Storage**: Passwords never stored in plaintext

## 🔄 Order Processing Flow

### Creating an Order
1. User sends order request with product items
2. System validates:
   - Product exists and is ACTIVE
   - Sufficient stock available for each item
3. Stock is reduced automatically
4. Order status set to CONFIRMED
5. Response includes order details and total amount

### Cancelling an Order
1. User sends cancel request for their order
2. System validates:
   - Order exists and belongs to user
   - Order status allows cancellation
3. Stock is restored for all items
4. Order status set to CANCELLED
5. User confirmation returned

## 📊 Database Schema

Automatic schema creation via Hibernate (`spring.jpa.hibernate.ddl-auto=update`)

Tables created:
- `users` - User accounts with roles
- `categories` - Product categories
- `products` - Product catalog
- `orders` - Customer orders
- `order_items` - Order line items

## ⚙️ Configuration

### JWT Settings
```properties
app.jwt.expirationMinutes=60
app.jwt.secret=i-am-a-super-secret-i-am-a-super-secret-i-am-a-super-secret
```

**For Production**: 
- Use environment variables for JWT secret
- Change to strong random string (256+ bits)
- Use secure vault (AWS Secrets Manager, HashiCorp Vault, etc.)

### Database
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/subscription_manager
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
```

## 🧪 Testing

### Manual Testing with cURL
See [TEST_AUTH.sh](TEST_AUTH.sh) for comprehensive testing scenarios

### Key Test Scenarios
1. User registration and login
2. Admin creating categories and products
3. Stock validation during order placement
4. Insufficient stock error handling
5. Order cancellation with stock restoration
6. Role-based access control (403 errors)

## 🐛 Error Handling

| HTTP Status | Meaning | Example |
|------------|---------|---------|
| 201 Created | Resource created successfully | Category created |
| 204 No Content | Request successful, no content | Order cancelled |
| 400 Bad Request | Invalid request data | Duplicate username |
| 401 Unauthorized | Auth failed or invalid token | Wrong password |
| 403 Forbidden | Insufficient permissions | USER accessing ADMIN endpoint |
| 404 Not Found | Resource doesn't exist | Category not found |

## 📈 Future Enhancements

- [ ] Email notifications for order status
- [ ] Payment integration (Stripe, PayPal)
- [ ] Order tracking and shipping
- [ ] Admin analytics dashboard
- [ ] Product reviews and ratings
- [ ] Wishlist feature
- [ ] Discount codes and coupons
- [ ] Two-factor authentication
- [ ] Order status webhooks
- [ ] Inventory forecasting

## 📝 Documentation

- [API_DOCUMENTATION.md](API_DOCUMENTATION.md) - Complete API reference
- [AUTHENTICATION.md](AUTHENTICATION.md) - Authentication system details
- [TEST_AUTH.sh](TEST_AUTH.sh) - Testing guide with examples

## 🤝 Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit pull request

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details

## 👥 Support

For issues, questions, or contributions, please open an issue on the repository.

## 🎯 Project Status

✅ **Complete** - All functional requirements implemented and tested

### Implemented Features
- ✅ JWT Authentication with BCrypt
- ✅ Role-Based Authorization
- ✅ Category Management
- ✅ Product Management
- ✅ Inventory Management
- ✅ Order Management
- ✅ Stock Validation & Reduction
- ✅ Filtering & Searching
- ✅ Pagination
- ✅ Transactional Consistency
