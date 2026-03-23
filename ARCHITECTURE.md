# System Architecture Diagram - Multi-Tenant Authentication

## High-Level Architecture

```
┌──────────────────────────────────────────────────────────────────────────┐
│                            CLIENT APPLICATION                             │
│                    (Web, Mobile, Desktop, API Consumer)                   │
└───────────────────────────────┬──────────────────────────────────────────┘
                                │
                                │ HTTP/HTTPS
                                │ Authorization: Bearer <JWT>
                                ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                         SPRING BOOT APPLICATION                           │
│                            (Port 5000)                                    │
├──────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │              SECURITY LAYER (Spring Security)                    │   │
│  │                                                                   │   │
│  │  ┌──────────────────────────────────────────────────────────┐  │   │
│  │  │         JwtAuthenticationFilter                           │  │   │
│  │  │  • Extract JWT from Authorization header                 │  │   │
│  │  │  • Validate token signature & expiration                 │  │   │
│  │  │  • Extract user email & tenant identifier                │  │   │
│  │  │  • Set TenantContext (thread-local)                      │  │   │
│  │  │  • Load UserDetails & set SecurityContext                │  │   │
│  │  └──────────────────────────────────────────────────────────┘  │   │
│  │                              ▼                                    │   │
│  │  ┌──────────────────────────────────────────────────────────┐  │   │
│  │  │         SecurityConfig                                    │  │   │
│  │  │  • Configure authentication provider                     │  │   │
│  │  │  • Define public/protected endpoints                     │  │   │
│  │  │  • Enable method-level security                          │  │   │
│  │  │  • Configure CORS                                        │  │   │
│  │  └──────────────────────────────────────────────────────────┘  │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                              ▼                                            │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │              CONTROLLER LAYER (REST API)                         │   │
│  │                                                                   │   │
│  │  AuthController          UserController                          │   │
│  │  ├─ /api/auth/login      ├─ /api/user/me                        │   │
│  │  ├─ /api/auth/register   ├─ /api/user/profile                   │   │
│  │  ├─ /api/auth/tenant/register                                    │   │
│  │  └─ /api/auth/health                                             │   │
│  │                                                                   │   │
│  │  @PreAuthorize("hasAuthority('PERMISSION')")                     │   │
│  │  @PreAuthorize("hasRole('ROLE')")                                │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                              ▼                                            │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │              SERVICE LAYER (Business Logic)                      │   │
│  │                                                                   │   │
│  │  AuthService                  CustomUserDetailsService           │   │
│  │  ├─ login()                   ├─ loadUserByUsername()            │   │
│  │  ├─ register()                └─ loadUserByEmailAndTenant()      │   │
│  │  ├─ registerTenant()                                             │   │
│  │  │                                                                │   │
│  │  └─ Uses: JwtTokenProvider, PasswordEncoder, AuthenticationMgr   │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                              ▼                                            │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │              REPOSITORY LAYER (Data Access)                      │   │
│  │                                                                   │   │
│  │  UserRepository          TenantRepository                        │   │
│  │  RoleRepository          PermissionRepository                    │   │
│  │                                                                   │   │
│  │  (Spring Data JPA - Generates implementations)                   │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                              ▼                                            │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │              ENTITY LAYER (Domain Models)                        │   │
│  │                                                                   │   │
│  │  User ──┬──< Tenant                                              │   │
│  │         └──< Role ──< Permission                                 │   │
│  │                                                                   │   │
│  │  (JPA Entities with Hibernate)                                   │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                                                           │
└───────────────────────────────┬───────────────────────────────────────────┘
                                │
                                │ JDBC
                                ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                         MYSQL DATABASE (Port 3306)                        │
│                          Database: rentalBusiness                         │
├──────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  Tables:                                                                  │
│  ├─ tenants              (Tenant organizations)                          │
│  ├─ users                (Users, tenant-scoped)                          │
│  ├─ roles                (User roles)                                    │
│  ├─ permissions          (Granular permissions)                          │
│  ├─ user_roles           (Many-to-Many: User ↔ Role)                    │
│  └─ role_permissions     (Many-to-Many: Role ↔ Permission)              │
│                                                                           │
└──────────────────────────────────────────────────────────────────────────┘
```

## Request Flow: Authentication

```
1. Client Login Request
   │
   ▼
   POST /api/auth/login
   {
     "email": "user@company.com",
     "password": "password123",
     "tenantIdentifier": "company-abc"
   }
   │
   ▼
2. AuthController.login()
   │
   ▼
3. AuthService.login()
   │
   ├─ Validate tenant exists & active
   │
   ├─ Find user by email + tenant
   │
   ├─ Authenticate via AuthenticationManager
   │  └─ CustomUserDetailsService.loadUserByEmailAndTenant()
   │     └─ UserRepository.findByEmailAndTenantTenantIdentifier()
   │
   ├─ Update user.lastLoginAt
   │
   ├─ JwtTokenProvider.generateToken()
   │  └─ Create JWT with:
   │     • userId, email
   │     • tenantId, tenantIdentifier
   │     • roles, permissions
   │     • expiration (24h)
   │
   └─ Return AuthResponse with token
      │
      ▼
4. Response to Client
   {
     "token": "eyJhbGciOiJIUzUxMiJ9...",
     "userId": 1,
     "email": "user@company.com",
     "tenantIdentifier": "company-abc",
     "roles": ["USER"],
     "permissions": ["PROPERTY_READ", "BOOKING_CREATE", ...],
     "expiresAt": "2026-03-06T12:00:00"
   }
```

## Request Flow: Authenticated Request

```
1. Client Request with Token
   │
   ▼
   GET /api/user/profile
   Headers: {
     Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
   }
   │
   ▼
2. JwtAuthenticationFilter.doFilterInternal()
   │
   ├─ Extract JWT from Authorization header
   │
   ├─ JwtTokenProvider.validateToken()
   │  └─ Verify signature & expiration
   │
   ├─ Extract claims from token:
   │  • email
   │  • tenantIdentifier
   │  • roles
   │  • permissions
   │
   ├─ TenantContext.setTenantIdentifier()
   │  └─ Store in ThreadLocal for request scope
   │
   ├─ CustomUserDetailsService.loadUserByEmailAndTenant()
   │  └─ Load full user object from database
   │
   ├─ Create Authentication object
   │
   └─ SecurityContextHolder.getContext().setAuthentication()
      │
      ▼
3. Spring Security Authorization
   │
   ├─ Check @PreAuthorize annotations
   │
   ├─ Verify user has required roles/permissions
   │
   └─ Allow or Deny request
      │
      ▼
4. Controller Method Execution
   │
   ├─ Access current user:
   │  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   │  User user = (User) auth.getPrincipal();
   │
   ├─ Access tenant context:
   │  String tenantId = TenantContext.getTenantIdentifier();
   │
   └─ Execute business logic
      │
      ▼
5. Service Layer
   │
   ├─ Use tenant context for data filtering
   │
   └─ Call repository with tenant filter
      │
      ▼
6. Repository Layer
   │
   └─ Query database with tenant filter
      │
      ▼
7. Response to Client
   │
   └─ TenantContext.clear() (in filter finally block)
```

## Multi-Tenancy Isolation

```
┌────────────────────────────────────────────────────────────────┐
│                        DATABASE                                 │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │  Tenant: company-abc (ID: 1)                            │  │
│  │  ├─ Users                                               │  │
│  │  │  ├─ admin@company-abc.com                           │  │
│  │  │  └─ user@company-abc.com                            │  │
│  │  ├─ Properties                                          │  │
│  │  ├─ Bookings                                            │  │
│  │  └─ Payments                                            │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │  Tenant: another-company (ID: 2)                        │  │
│  │  ├─ Users                                               │  │
│  │  │  └─ admin@another-company.com                       │  │
│  │  ├─ Properties                                          │  │
│  │  ├─ Bookings                                            │  │
│  │  └─ Payments                                            │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                 │
└────────────────────────────────────────────────────────────────┘

Isolation Mechanism:
• Every user belongs to exactly one tenant
• JWT token contains tenantIdentifier
• TenantContext stores current tenant (thread-local)
• All queries automatically filter by tenant
• No cross-tenant data access possible
```

## Security & Authorization Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                  USER AUTHENTICATION                             │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                  ROLE ASSIGNMENT                                 │
│                                                                  │
│  User ──< UserRole >── Role                                     │
│                          │                                       │
│  Example:                │                                       │
│  john@company.com ───> USER                                     │
│  admin@company.com ───> TENANT_ADMIN                            │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                  PERMISSION ASSIGNMENT                           │
│                                                                  │
│  Role ──< RolePermission >── Permission                         │
│                                    │                             │
│  Example:                          │                             │
│  USER ───────────────> PROPERTY_READ                            │
│                   ───> BOOKING_CREATE                            │
│                   ───> BOOKING_READ                              │
│                                                                  │
│  TENANT_ADMIN ───────> USER_CREATE                              │
│               ───────> USER_UPDATE                               │
│               ───────> PROPERTY_CREATE                           │
│               ───────> ... (all tenant management)               │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                  ENDPOINT AUTHORIZATION                          │
│                                                                  │
│  @PreAuthorize("hasAuthority('PROPERTY_CREATE')")              │
│  public ResponseEntity<?> createProperty() {                    │
│      // Only users with PROPERTY_CREATE permission              │
│      // can execute this method                                 │
│  }                                                              │
│                                                                  │
│  @PreAuthorize("hasRole('TENANT_ADMIN')")                      │
│  public ResponseEntity<?> manageUsers() {                       │
│      // Only users with TENANT_ADMIN role                       │
│      // can execute this method                                 │
│  }                                                              │
└─────────────────────────────────────────────────────────────────┘
```

## JWT Token Structure

```
┌──────────────────────────────────────────────────────────────┐
│                      JWT TOKEN                                │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  Header                                                       │
│  {                                                            │
│    "alg": "HS512",                                           │
│    "typ": "JWT"                                              │
│  }                                                            │
│                                                               │
│  ═══════════════════════════════════════════════════════════ │
│                                                               │
│  Payload (Claims)                                            │
│  {                                                            │
│    "sub": "user@company.com",        // Subject (email)      │
│    "userId": 1,                                              │
│    "email": "user@company.com",                              │
│    "tenantId": 1,                                            │
│    "tenantIdentifier": "company-abc",                        │
│    "roles": ["USER"],                                        │
│    "permissions": [                                          │
│      "PROPERTY_READ",                                        │
│      "BOOKING_CREATE",                                       │
│      "BOOKING_READ",                                         │
│      ...                                                     │
│    ],                                                        │
│    "iat": 1709640000,                // Issued at           │
│    "exp": 1709726400                 // Expires at          │
│  }                                                            │
│                                                               │
│  ═══════════════════════════════════════════════════════════ │
│                                                               │
│  Signature                                                    │
│  HMACSHA512(                                                 │
│    base64UrlEncode(header) + "." +                          │
│    base64UrlEncode(payload),                                │
│    secret                                                    │
│  )                                                            │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

## Component Dependencies

```
┌─────────────────────────────────────────────────────────────────┐
│                     Spring Boot Application                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  JwtAuthenticationFilter                                        │
│  ├─ depends on → JwtTokenProvider                              │
│  └─ depends on → CustomUserDetailsService                      │
│                                                                  │
│  SecurityConfig                                                 │
│  ├─ depends on → JwtAuthenticationFilter                       │
│  ├─ depends on → CustomUserDetailsService                      │
│  └─ depends on → PasswordEncoder (BCrypt)                      │
│                                                                  │
│  AuthService                                                    │
│  ├─ depends on → UserRepository                                │
│  ├─ depends on → TenantRepository                              │
│  ├─ depends on → RoleRepository                                │
│  ├─ depends on → JwtTokenProvider                              │
│  ├─ depends on → PasswordEncoder                               │
│  └─ depends on → AuthenticationManager                         │
│                                                                  │
│  CustomUserDetailsService                                       │
│  ├─ depends on → UserRepository                                │
│  └─ depends on → TenantContext                                 │
│                                                                  │
│  DataInitializer                                                │
│  ├─ depends on → RoleRepository                                │
│  └─ depends on → PermissionRepository                          │
│                                                                  │
│  TenantContext (ThreadLocal)                                    │
│  └─ No dependencies                                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---
This architecture provides a robust, scalable, and secure foundation for
building multi-tenant applications with complete data isolation.

