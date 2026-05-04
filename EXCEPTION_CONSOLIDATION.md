# Exception Consolidation Guide

## Overview
All custom exception classes and the error response class have been consolidated into a single file: `GlobalExceptionHandler.java`

## Changes Made

### Files Removed
The following individual exception files have been removed:
- ❌ `ErrorResponse.java`
- ❌ `ResourceNotFoundException.java`
- ❌ `DuplicateResourceException.java`
- ❌ `InvalidCredentialsException.java`
- ❌ `InsufficientInventoryException.java`
- ❌ `BusinessLogicException.java`
- ❌ `UserAccountException.java`

### Files Consolidated
All exceptions and the error response are now defined in:
- ✅ `GlobalExceptionHandler.java`

## Exception Classes (Now All in GlobalExceptionHandler.java)

### 1. ErrorResponse
Response class for all error responses sent to clients.
```java
public class ErrorResponse {
    private int status;
    private String message;
    private String errorCode;
    private long timestamp;
}
```

### 2. ResourceNotFoundException
Thrown when a requested resource is not found.
```java
public class ResourceNotFoundException extends RuntimeException
```

### 3. DuplicateResourceException
Thrown when attempting to create a duplicate resource.
```java
public class DuplicateResourceException extends RuntimeException
```

### 4. InvalidCredentialsException
Thrown for invalid authentication credentials.
```java
public class InvalidCredentialsException extends RuntimeException
```

### 5. InsufficientInventoryException
Thrown when insufficient inventory is available for an order.
```java
public class InsufficientInventoryException extends RuntimeException
```

### 6. BusinessLogicException
Thrown when an operation violates business rules or constraints.
```java
public class BusinessLogicException extends RuntimeException
```

### 7. UserAccountException
Thrown when a user account is inactive or disabled.
```java
public class UserAccountException extends RuntimeException
```

## Usage Across the Application

All services and controllers can import exceptions using:
```java
import com.guvi.inventory.exception.*;
```

Or import specific exceptions:
```java
import com.guvi.inventory.exception.ResourceNotFoundException;
import com.guvi.inventory.exception.DuplicateResourceException;
import com.guvi.inventory.exception.InvalidCredentialsException;
import com.guvi.inventory.exception.InsufficientInventoryException;
import com.guvi.inventory.exception.BusinessLogicException;
import com.guvi.inventory.exception.UserAccountException;
import com.guvi.inventory.exception.ErrorResponse;
```

## Exception Handling

The `GlobalExceptionHandler` class is annotated with `@RestControllerAdvice` and handles all exceptions globally:

- `@ExceptionHandler(ResourceNotFoundException.class)` → HTTP 404 NOT_FOUND
- `@ExceptionHandler(DuplicateResourceException.class)` → HTTP 409 CONFLICT
- `@ExceptionHandler(InvalidCredentialsException.class)` → HTTP 401 UNAUTHORIZED
- `@ExceptionHandler(InsufficientInventoryException.class)` → HTTP 400 BAD_REQUEST
- `@ExceptionHandler(BusinessLogicException.class)` → HTTP 400 BAD_REQUEST
- `@ExceptionHandler(UserAccountException.class)` → HTTP 403 FORBIDDEN
- `@ExceptionHandler(AccessDeniedException.class)` → HTTP 403 FORBIDDEN
- `@ExceptionHandler(IllegalArgumentException.class)` → HTTP 400 BAD_REQUEST
- `@ExceptionHandler(Exception.class)` → HTTP 500 INTERNAL_SERVER_ERROR

## Benefits

1. **Centralized Exception Management**: All exceptions are in one place, making it easier to understand and maintain the exception hierarchy.

2. **Single File to Update**: When modifying exception behavior, developers only need to edit one file.

3. **Reduced File Count**: Cleaner project structure with fewer files to manage.

4. **Consistent Exception Handling**: All exceptions are handled by the same global handler.

5. **Easy to Import**: All exceptions are accessible through one package import.

## Migration Notes

If you have existing code that imports individual exception classes from separate files, they will automatically work since:
- All exceptions are now defined in the same package (`com.guvi.inventory.exception`)
- All exception classes are now `public` (previously `public` in individual files)
- The fully qualified class names remain the same

No code changes are required for existing imports!

## File Structure

```
src/main/java/com/guvi/inventory/exception/
└── GlobalExceptionHandler.java
    ├── class ErrorResponse
    ├── class ResourceNotFoundException
    ├── class DuplicateResourceException
    ├── class InvalidCredentialsException
    ├── class InsufficientInventoryException
    ├── class BusinessLogicException
    ├── class UserAccountException
    └── class GlobalExceptionHandler (Handler)
```

