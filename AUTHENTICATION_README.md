# Multi-Tenant Authentication System - Rental Business

## Overview

This is a comprehensive multi-tenant authentication and authorization system built with Spring Boot 3.5, Spring Security, JWT, and MySQL. The system supports complete tenant isolation with role-based access control (RBAC).

## Features

### 🔐 Authentication
- JWT-based authentication
- Secure password encryption (BCrypt)
- Token-based session management
- Account lockout after failed login attempts
- Email verification support

### 🏢 Multi-Tenancy
- Complete tenant isolation
- Tenant-specific user management
- Tenant context in every request
- Support for multiple tenants in single database

### 👥 User Management
- User registration with tenant association
- User profiles with roles and permissions
- Account status management (active/inactive)
- Failed login attempt tracking

### 🎯 Authorization (RBAC)
- Role-Based Access Control
- Granular permission system
- Pre-defined roles:
  - **SUPER_ADMIN**: System-wide administrator
  - **TENANT_ADMIN**: Tenant administrator
  - **MANAGER**: Property manager
  - **USER**: Regular user

### 📊 Permission System
Resources covered:
- USER (Create, Read, Update, Delete)
- PROPERTY (Create, Read, Update, Delete)
- BOOKING (Create, Read, Update, Delete)
- PAYMENT (Create, Read, Update, Delete)
- REVIEW (Create, Read, Update, Delete)
- TENANT (Read, Update)

## Architecture

### Entity Relationship
```
Tenant (1) ──< (N) User
User (N) >──< (N) Role
Role (N) >──< (N) Permission
```

### Key Components

#### Models
- `Tenant`: Represents a tenant organization
- `User`: User entity with multi-tenant support
- `Role`: Role entity with permissions
- `Permission`: Granular permission entity

#### Services
- `AuthService`: Handles authentication and registration
- `CustomUserDetailsService`: Loads user details for Spring Security
- `JwtTokenProvider`: JWT token generation and validation

#### Security
- `SecurityConfig`: Spring Security configuration
- `JwtAuthenticationFilter`: JWT token validation filter
- `TenantContext`: Thread-local tenant context holder

## API Endpoints

### Public Endpoints

#### 1. Register Tenant
```http
POST /api/auth/tenant/register
Content-Type: application/json

{
  "tenantIdentifier": "company-abc",
  "name": "Company ABC",
  "description": "A rental company",
  "contactEmail": "contact@company-abc.com",
  "contactPhone": "+1234567890",
  "address": "123 Main St",
  "adminEmail": "admin@company-abc.com",
  "adminPassword": "SecurePass123",
  "adminFirstName": "John",
  "adminLastName": "Doe",
  "adminPhone": "+1234567890"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "admin@company-abc.com",
  "firstName": "John",
  "lastName": "Doe",
  "tenantIdentifier": "company-abc",
  "roles": ["TENANT_ADMIN"],
  "permissions": ["USER_CREATE", "USER_READ", ...],
  "expiresAt": "2026-03-06T12:00:00"
}
```

#### 2. User Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@company-abc.com",
  "password": "password123",
  "tenantIdentifier": "company-abc"
}
```

**Response:** Same as registration response

#### 3. Register User (within existing tenant)
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@company-abc.com",
  "password": "password123",
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "+1234567890",
  "tenantIdentifier": "company-abc"
}
```

**Response:**
```json
{
  "id": 2,
  "email": "user@company-abc.com",
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "+1234567890",
  "tenantIdentifier": "company-abc",
  "isActive": true,
  "emailVerified": false,
  "roles": ["USER"],
  "createdAt": "2026-03-05T10:30:00"
}
```

### Protected Endpoints

#### 4. Get Current User
```http
GET /api/user/me
Authorization: Bearer <token>
```

**Response:**
```json
{
  "id": 1,
  "email": "user@company-abc.com",
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "+1234567890",
  "tenantIdentifier": "company-abc",
  "isActive": true,
  "emailVerified": true,
  "roles": ["USER"],
  "createdAt": "2026-03-05T10:30:00",
  "lastLoginAt": "2026-03-05T12:00:00"
}
```

#### 5. Get User Profile
```http
GET /api/user/profile
Authorization: Bearer <token>
```

**Response:**
```json
{
  "id": 1,
  "email": "user@company-abc.com",
  "fullName": "Jane Smith",
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "+1234567890",
  "tenant": "Company ABC",
  "tenantIdentifier": "company-abc",
  "roles": ["USER"],
  "permissions": ["PROPERTY_READ", "BOOKING_CREATE", ...],
  "createdAt": "2026-03-05T10:30:00",
  "lastLoginAt": "2026-03-05T12:00:00"
}
```

#### 6. Health Check
```http
GET /api/auth/health
```

## Configuration

### JWT Configuration (application.properties)
```properties
# JWT secret key (base64 encoded - change in production!)
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970337336763979244226452948404D635166546A576E5A7234753778214125442A

# JWT expiration time (24 hours in milliseconds)
jwt.expiration=86400000

# JWT refresh token expiration (7 days in milliseconds)
jwt.refresh-expiration=604800000
```

### Database Setup

1. Create MySQL database:
```sql
CREATE DATABASE rentalBusiness;
```

2. The application will automatically create tables on startup with `spring.jpa.hibernate.ddl-auto=update`

3. Default roles and permissions are initialized automatically via `DataInitializer`

## Security Features

### 1. Password Security
- Passwords are encrypted using BCrypt
- Minimum 8 characters required
- Passwords are never stored in plain text

### 2. Account Lockout
- Account locked after 5 failed login attempts
- Failed attempts counter reset on successful login
- Locked accounts require admin intervention

### 3. JWT Token Security
- Tokens signed with HMAC-SHA512
- Token includes user, tenant, roles, and permissions
- Token expiration enforced
- Token validation on every request

### 4. Tenant Isolation
- Every request must include tenant context
- Users can only access data within their tenant
- Tenant identifier included in JWT token
- TenantContext ensures thread-safe tenant isolation

### 5. CORS Configuration
- Pre-configured for local development (localhost:3000, localhost:5173)
- Modify `SecurityConfig.corsConfigurationSource()` for production

## Usage Examples

### Example 1: Register a New Tenant
```bash
curl -X POST http://localhost:5000/api/auth/tenant/register \
  -H "Content-Type: application/json" \
  -d '{
    "tenantIdentifier": "acme-corp",
    "name": "ACME Corporation",
    "adminEmail": "admin@acme.com",
    "adminPassword": "Admin123!",
    "adminFirstName": "Admin",
    "adminLastName": "User"
  }'
```

### Example 2: Login
```bash
curl -X POST http://localhost:5000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@acme.com",
    "password": "Admin123!",
    "tenantIdentifier": "acme-corp"
  }'
```

### Example 3: Access Protected Endpoint
```bash
curl -X GET http://localhost:5000/api/user/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## Authorization in Controllers

Use Spring Security annotations for authorization:

```java
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @PostMapping
    @PreAuthorize("hasAuthority('PROPERTY_CREATE')")
    public ResponseEntity<?> createProperty() {
        // Only users with PROPERTY_CREATE permission can access
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PROPERTY_READ')")
    public ResponseEntity<?> getProperties() {
        // Only users with PROPERTY_READ permission can access
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PROPERTY_UPDATE')")
    public ResponseEntity<?> updateProperty() {
        // Only users with PROPERTY_UPDATE permission can access
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteProperty() {
        // Only admins can delete
    }
}
```

## Accessing Current User and Tenant

```java
@RestController
public class ExampleController {

    @GetMapping("/example")
    public ResponseEntity<?> example() {
        // Get authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        // Get tenant identifier
        String tenantId = TenantContext.getTenantIdentifier();
        
        // Access user properties
        String email = currentUser.getEmail();
        String tenantName = currentUser.getTenant().getName();
        Set<Role> roles = currentUser.getRoles();
        
        return ResponseEntity.ok("Success");
    }
}
```

## Error Handling

The system includes comprehensive error handling with appropriate HTTP status codes:

- `400 Bad Request`: Validation errors
- `401 Unauthorized`: Invalid credentials or token
- `404 Not Found`: User or tenant not found
- `409 Conflict`: User or tenant already exists
- `500 Internal Server Error`: Server errors

Example error response:
```json
{
  "status": 401,
  "message": "Invalid email or password",
  "error": "Invalid Credentials",
  "timestamp": "2026-03-05T12:00:00",
  "path": "/api/auth/login"
}
```

## Testing the API

### 1. Start the Application
```bash
mvn spring-boot:run
```

### 2. Register First Tenant
Use the tenant registration endpoint to create your first tenant and admin user.

### 3. Login
Login with the admin credentials to get a JWT token.

### 4. Access Protected Resources
Use the JWT token in the Authorization header for all protected endpoints.

## Production Considerations

### 1. Change JWT Secret
Generate a new secure secret key:
```bash
# Generate a secure random key
openssl rand -base64 64
```
Update `jwt.secret` in application.properties

### 2. Use Environment Variables
```properties
jwt.secret=${JWT_SECRET}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

### 3. Enable HTTPS
Configure SSL/TLS certificates in production

### 4. Update CORS Configuration
Restrict allowed origins to your frontend domain

### 5. Database Migration
Consider using Flyway or Liquibase for production database migrations instead of `ddl-auto=update`

### 6. Logging
Reduce logging level in production:
```properties
logging.level.com.backend.rentalBusiness=INFO
logging.level.org.springframework.security=WARN
```

### 7. Rate Limiting
Implement rate limiting to prevent brute force attacks

## Troubleshooting

### Issue: 401 Unauthorized on protected endpoints
- Check if JWT token is included in Authorization header
- Verify token format: `Bearer <token>`
- Ensure token hasn't expired
- Check if user account is active and verified

### Issue: User not found
- Verify tenant identifier matches
- Check if tenant is active
- Confirm user exists in database

### Issue: Permission denied
- Verify user has required role or permission
- Check role and permission assignments in database
- Review `@PreAuthorize` annotations

## License

This project is part of a rental business application.

## Support

For issues and questions, please refer to the project documentation or contact the development team.

