# Production Deployment Checklist

## ✅ Pre-Deployment Checklist

### 🔐 Security

- [ ] **Change JWT Secret Key**
  ```bash
  # Generate a new secure secret
  openssl rand -base64 64
  ```
  Update `jwt.secret` in production configuration

- [ ] **Use Environment Variables**
  ```properties
  # Don't hardcode sensitive values
  jwt.secret=${JWT_SECRET:default-for-dev}
  spring.datasource.url=${DB_URL}
  spring.datasource.username=${DB_USERNAME}
  spring.datasource.password=${DB_PASSWORD}
  ```

- [ ] **Enable HTTPS/SSL**
  - Obtain SSL certificate (Let's Encrypt, etc.)
  - Configure in `application.properties`:
    ```properties
    server.ssl.enabled=true
    server.ssl.key-store=classpath:keystore.p12
    server.ssl.key-store-password=${KEYSTORE_PASSWORD}
    server.ssl.key-store-type=PKCS12
    ```

- [ ] **Update CORS Configuration**
  ```java
  // In SecurityConfig.java
  configuration.setAllowedOrigins(List.of(
      "https://yourdomain.com",
      "https://app.yourdomain.com"
  ));
  ```

- [ ] **Disable Debug Logging**
  ```properties
  logging.level.com.backend.rentalBusiness=INFO
  logging.level.org.springframework.security=WARN
  logging.level.org.hibernate.SQL=WARN
  ```

- [ ] **Hide SQL Statements**
  ```properties
  spring.jpa.show-sql=false
  spring.jpa.properties.hibernate.format_sql=false
  ```

- [ ] **Implement Rate Limiting**
  - Use Spring Cloud Gateway or Bucket4j
  - Limit login attempts per IP
  - Limit API calls per user/tenant

- [ ] **Add Request Validation**
  - Enable strict validation
  - Sanitize all inputs
  - Validate file uploads

### 🗄️ Database

- [ ] **Use Production Database**
  - MySQL 8.0+ in production environment
  - Strong password for database user
  - Restricted network access

- [ ] **Change DDL Auto Strategy**
  ```properties
  # Don't use 'update' in production
  spring.jpa.hibernate.ddl-auto=none
  ```

- [ ] **Implement Database Migrations**
  - Use Flyway or Liquibase
  - Version control your schema changes
  - Test migrations before deployment

- [ ] **Setup Database Backups**
  - Automated daily backups
  - Point-in-time recovery
  - Test restore procedures

- [ ] **Database Connection Pool**
  ```properties
  spring.datasource.hikari.maximum-pool-size=20
  spring.datasource.hikari.minimum-idle=5
  spring.datasource.hikari.connection-timeout=30000
  spring.datasource.hikari.idle-timeout=600000
  spring.datasource.hikari.max-lifetime=1800000
  ```

- [ ] **Create Database Indexes**
  ```sql
  CREATE INDEX idx_users_email_tenant ON users(email, tenant_id);
  CREATE INDEX idx_users_tenant ON users(tenant_id);
  CREATE INDEX idx_tenants_identifier ON tenants(tenant_identifier);
  ```

### ⚙️ Application Configuration

- [ ] **Production Profile**
  Create `application-prod.properties`:
  ```properties
  spring.profiles.active=prod
  server.port=8080
  
  # Use environment variables for sensitive data
  jwt.secret=${JWT_SECRET}
  spring.datasource.url=${DB_URL}
  spring.datasource.username=${DB_USERNAME}
  spring.datasource.password=${DB_PASSWORD}
  ```

- [ ] **Configure JVM Options**
  ```bash
  -Xms512m -Xmx2048m
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=200
  -Dspring.profiles.active=prod
  ```

- [ ] **Configure Timeouts**
  ```properties
  server.connection-timeout=20000
  spring.mvc.async.request-timeout=30000
  ```

### 📊 Monitoring & Logging

- [ ] **Setup Application Logging**
  ```properties
  logging.file.name=/var/log/rental-business/application.log
  logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
  logging.level.root=WARN
  logging.level.com.backend.rentalBusiness=INFO
  ```

- [ ] **Setup Log Rotation**
  ```properties
  logging.logback.rollingpolicy.max-file-size=10MB
  logging.logback.rollingpolicy.max-history=30
  ```

- [ ] **Add Spring Boot Actuator**
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  ```
  
  ```properties
  management.endpoints.web.exposure.include=health,info,metrics
  management.endpoint.health.show-details=when-authorized
  ```

- [ ] **Setup Application Monitoring**
  - Use Prometheus + Grafana
  - Or use New Relic, DataDog, etc.
  - Monitor CPU, memory, database connections
  - Track API response times

- [ ] **Setup Error Tracking**
  - Use Sentry, Rollbar, or similar
  - Track exceptions and errors
  - Alert on critical errors

- [ ] **Log Security Events**
  - Failed login attempts
  - Account lockouts
  - Permission denied events
  - Suspicious activities

### 🚀 Deployment

- [ ] **Build Application**
  ```bash
  mvn clean package -DskipTests=false
  ```

- [ ] **Run Tests**
  ```bash
  mvn test
  ```

- [ ] **Create Docker Image** (Optional)
  ```dockerfile
  FROM openjdk:17-jdk-slim
  WORKDIR /app
  COPY target/rentalBusiness-0.0.1-SNAPSHOT.jar app.jar
  EXPOSE 8080
  ENTRYPOINT ["java", "-jar", "app.jar"]
  ```

- [ ] **Setup Reverse Proxy**
  - Use Nginx or Apache
  - Configure SSL termination
  - Setup load balancing if needed

- [ ] **Setup Process Manager**
  - Use systemd, supervisor, or PM2
  - Auto-restart on failure
  - Start on system boot

### 📧 Additional Features

- [ ] **Email Service Integration**
  - Configure SMTP settings
  - Email verification
  - Password reset emails
  - Notification emails

- [ ] **Implement Refresh Tokens**
  - Add refresh token endpoint
  - Store refresh tokens securely
  - Implement token rotation

- [ ] **Add Password Reset**
  - Forgot password endpoint
  - Secure token generation
  - Email with reset link
  - Time-limited reset tokens

- [ ] **Add Email Verification**
  - Send verification email on registration
  - Verification endpoint
  - Resend verification email

- [ ] **Implement Account Management**
  - Change password
  - Update profile
  - Delete account
  - Account activity log

### 🧪 Testing

- [ ] **Unit Tests**
  - Test all services
  - Test repositories
  - Test utilities

- [ ] **Integration Tests**
  - Test API endpoints
  - Test authentication flow
  - Test authorization

- [ ] **Security Tests**
  - Test SQL injection protection
  - Test XSS protection
  - Test CSRF protection
  - Test authentication bypass attempts

- [ ] **Load Testing**
  - Use JMeter, Gatling, or k6
  - Test concurrent users
  - Test database performance
  - Identify bottlenecks

- [ ] **Penetration Testing**
  - Professional security audit
  - Vulnerability scanning
  - Fix identified issues

### 📄 Documentation

- [ ] **API Documentation**
  - Add Swagger/OpenAPI
  - Document all endpoints
  - Include examples

- [ ] **User Documentation**
  - How to register
  - How to login
  - API usage guide

- [ ] **Admin Documentation**
  - Deployment guide
  - Configuration guide
  - Troubleshooting guide
  - Backup/Restore procedures

### 🔄 CI/CD

- [ ] **Setup CI/CD Pipeline**
  - GitHub Actions, GitLab CI, or Jenkins
  - Automated testing
  - Automated deployment

- [ ] **Setup Staging Environment**
  - Mirror production setup
  - Test before production
  - Use for UAT

### 🎯 Performance Optimization

- [ ] **Enable Response Caching**
  ```java
  @Cacheable("users")
  public User getUserById(Long id) { ... }
  ```

- [ ] **Optimize Database Queries**
  - Add indexes
  - Use query optimization
  - Avoid N+1 queries
  - Use pagination

- [ ] **Enable Compression**
  ```properties
  server.compression.enabled=true
  server.compression.mime-types=application/json,application/xml,text/html,text/xml,text/plain
  ```

- [ ] **Configure Connection Pool**
  - Optimize pool size
  - Monitor connection usage

### 🔒 Compliance

- [ ] **GDPR Compliance** (if applicable)
  - User data export
  - User data deletion
  - Privacy policy
  - Cookie consent

- [ ] **Terms of Service**
  - Create ToS document
  - Require acceptance on registration

- [ ] **Data Retention Policy**
  - Define data retention periods
  - Implement data cleanup

### 🆘 Disaster Recovery

- [ ] **Backup Strategy**
  - Database backups (daily)
  - Application backups
  - Configuration backups
  - Test restore procedures

- [ ] **Disaster Recovery Plan**
  - Document recovery procedures
  - Define RTO (Recovery Time Objective)
  - Define RPO (Recovery Point Objective)
  - Regular DR drills

- [ ] **High Availability** (Optional)
  - Multiple application instances
  - Database replication
  - Load balancing
  - Failover procedures

## 🎉 Post-Deployment

- [ ] **Smoke Tests**
  - Test critical paths
  - Verify all services running
  - Check database connectivity

- [ ] **Monitor Application**
  - Watch logs for errors
  - Monitor performance metrics
  - Check database connections

- [ ] **Setup Alerts**
  - High error rate
  - High response time
  - Database connection issues
  - Disk space warnings

- [ ] **Document Deployment**
  - Deployment date/time
  - Version deployed
  - Any issues encountered
  - Post-deployment actions

## 📝 Maintenance Checklist

### Daily
- [ ] Monitor application logs
- [ ] Check error rates
- [ ] Review failed login attempts

### Weekly
- [ ] Review performance metrics
- [ ] Check disk space
- [ ] Review security logs
- [ ] Update dependencies (if needed)

### Monthly
- [ ] Database backup verification
- [ ] Security patch updates
- [ ] Performance review
- [ ] Capacity planning review

### Quarterly
- [ ] Security audit
- [ ] Disaster recovery drill
- [ ] Performance optimization review
- [ ] Documentation updates

## 🚨 Emergency Contacts

```
Production Database: [DBA Contact]
Infrastructure: [DevOps Contact]
Security: [Security Team Contact]
On-call Engineer: [On-call Contact]
```

## 📞 Incident Response

1. **Identify** the issue
2. **Assess** the impact
3. **Notify** relevant stakeholders
4. **Mitigate** the immediate problem
5. **Resolve** the root cause
6. **Document** the incident
7. **Post-mortem** review

---

**Remember:** Security and reliability are ongoing processes, not one-time tasks!

Good luck with your deployment! 🚀

