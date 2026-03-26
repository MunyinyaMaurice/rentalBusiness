# Quick Start Guide - Multi-Tenant Authentication

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Step 1: Database Setup

1. Start MySQL server
2. Create the database:

```sql
CREATE DATABASE rentalBusiness;
```

3. Verify connection details in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rentalBusiness
spring.datasource.username=root
spring.datasource.password=root
```

## Step 2: Install Dependencies

```bash
mvn clean install
```

## Step 3: Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:5000`

## Step 4: Initial Setup - Register Your First Tenant

### Register a Tenant (Creates tenant + admin user)

**Request:**
```bash
curl -X POST http://localhost:5000/api/auth/tenant/register \
  -H "Content-Type: application/json" \
  -d '{
    "tenantIdentifier": "my-company",
    "name": "My Company Inc.",
    "description": "A rental business",
    "contactEmail": "contact@mycompany.com",
    "contactPhone": "+1234567890",
    "address": "123 Main St, City, Country",
    "adminEmail": "admin@mycompany.com",
    "adminPassword": "Admin123!",
    "adminFirstName": "Admin",
    "adminLastName": "User",
    "adminPhone": "+1234567890"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjEsImVtYWlsIjoiYWRtaW5AbXljb21wYW55LmNvbSIsInRlbmFudElkIjoxLCJ0ZW5hbnRJZGVudGlmaWVyIjoibXktY29tcGFueSIsInJvbGVzIjpbIlRFTkFOVF9BRE1JTiJdLCJwZXJtaXNzaW9ucyI6WyJVU0VSX0NSRUFURSIsIlVTRVJfUkVBRCIsIlVTRVJfVVBEQVRFIiwiVVNFUl9ERUxFVEUiLCJQUk9QRVJUWV9DUkVBVEUiLCJQUk9QRVJUWV9SRUFEIiwiUFJPUEVSVFlfVVBEQVRFIiwiUFJPUEVSVFlfREVMRVRFIiwiQk9PS0lOR19SRUFEIiwiQk9PS0lOR19VUERBVEUiLCJQQVlNRU5UX1JFQUQiLCJSRVZJRVdfUkVBRCIsIlJFVklFV19ERUxFVEUiLCJURU5BTlRfUkVBRCIsIlRFTkFOVF9VUERBVEUiXSwic3ViIjoiYWRtaW5AbXljb21wYW55LmNvbSIsImlhdCI6MTcwOTY0MDAwMCwiZXhwIjoxNzA5NzI2NDAwfQ.xyz...",
  "type": "Bearer",
  "userId": 1,
  "email": "admin@mycompany.com",
  "firstName": "Admin",
  "lastName": "User",
  "tenantIdentifier": "my-company",
  "roles": ["TENANT_ADMIN"],
  "permissions": ["USER_CREATE", "USER_READ", ...],
  "expiresAt": "2026-03-06T10:00:00"
}
```

**Save the token!** You'll need it for authenticated requests.

## Step 5: Test Authentication

### Login

```bash
curl -X POST http://localhost:5000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@mycompany.com",
    "password": "Admin123!",
    "tenantIdentifier": "my-company"
  }'
```

### Get Current User Profile

```bash
curl -X GET http://localhost:5000/api/user/me \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Get Detailed Profile

```bash
curl -X GET http://localhost:5000/api/user/profile \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Step 6: Register Regular Users

### Register a User (within your tenant)

```bash
curl -X POST http://localhost:5000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@mycompany.com",
    "password": "User123!",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890",
    "tenantIdentifier": "my-company"
  }'
```

## Step 7: Test Multi-Tenancy

### Create Another Tenant

```bash
curl -X POST http://localhost:5000/api/auth/tenant/register \
  -H "Content-Type: application/json" \
  -d '{
    "tenantIdentifier": "another-company",
    "name": "Another Company Ltd.",
    "adminEmail": "admin@anothercompany.com",
    "adminPassword": "Admin456!",
    "adminFirstName": "Jane",
    "adminLastName": "Smith"
  }'
```

### Verify Isolation

Try logging in with the same email but different tenant:
```bash
# Login to first tenant
curl -X POST http://localhost:5000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@mycompany.com",
    "password": "Admin123!",
    "tenantIdentifier": "my-company"
  }'

# Login to second tenant (should fail if user doesn't exist there)
curl -X POST http://localhost:5000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@mycompany.com",
    "password": "Admin123!",
    "tenantIdentifier": "another-company"
  }'
```

## Common Issues & Solutions

### Issue 1: Database Connection Error
**Error:** `Communications link failure`

**Solution:**
1. Verify MySQL is running: `mysql -u root -p`
2. Check connection details in `application.properties`
3. Create database if it doesn't exist

### Issue 2: 401 Unauthorized
**Error:** `Invalid email or password`

**Solution:**
1. Verify tenant identifier is correct
2. Check password is correct
3. Ensure user exists in that tenant
4. Check if account is locked (5 failed attempts lock the account)

### Issue 3: Token Expired
**Error:** `JWT token is expired`

**Solution:**
1. Login again to get a new token
2. Token expires after 24 hours (configurable)

### Issue 4: Port Already in Use
**Error:** `Port 5000 is already in use`

**Solution:**
1. Change port in `application.properties`: `server.port=8080`
2. Or stop the process using port 5000

## Testing with Postman

### Import Collection

1. Create a new Postman collection
2. Add environment variables:
   - `base_url`: `http://localhost:5000`
   - `token`: (will be set automatically after login)
   - `tenant_id`: `my-company`

### Pre-request Script (for authenticated requests)

Add this to requests that need authentication:
```javascript
pm.request.headers.add({
    key: 'Authorization',
    value: 'Bearer ' + pm.environment.get('token')
});
```

### Test Script (to save token after login)

Add this to login/register requests:
```javascript
if (pm.response.code === 200 || pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.environment.set('token', jsonData.token);
}
```

## Next Steps

1. **Implement Business Logic**: Create controllers for Properties, Bookings, Payments, etc.
2. **Use Tenant Context**: Access current tenant using `TenantContext.getTenantIdentifier()`
3. **Authorization**: Use `@PreAuthorize` annotations on controller methods
4. **Add More Roles**: Create custom roles specific to your business needs
5. **Email Verification**: Implement email verification for new users
6. **Password Reset**: Implement forgot password functionality
7. **Refresh Tokens**: Implement refresh token mechanism for better UX

## Security Best Practices

1. **Change JWT Secret**: Generate a new secret key for production
2. **Use HTTPS**: Always use HTTPS in production
3. **Rate Limiting**: Implement rate limiting to prevent brute force attacks
4. **Input Validation**: Always validate input data
5. **Logging**: Implement proper logging for security events
6. **Monitoring**: Monitor failed login attempts and suspicious activities

## Example: Using Authorization in Your Code

```java
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @GetMapping
    @PreAuthorize("hasAuthority('PROPERTY_READ')")
    public ResponseEntity<List<Property>> getProperties() {
        String tenantId = TenantContext.getTenantIdentifier();
        // Fetch properties for this tenant only
        return ResponseEntity.ok(propertyService.getPropertiesByTenant(tenantId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PROPERTY_CREATE')")
    public ResponseEntity<Property> createProperty(@RequestBody PropertyDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        String tenantId = currentUser.getTenant().getTenantIdentifier();
        
        // Create property for current tenant
        Property property = propertyService.create(dto, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(property);
    }
}
```

## Support

For more information, refer to:
- `AUTHENTICATION_README.md` - Comprehensive documentation
- Spring Security Documentation: https://docs.spring.io/spring-security/reference/
- JWT Documentation: https://jwt.io/

Happy coding! 🚀

