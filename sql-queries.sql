-- Useful SQL Queries for Multi-Tenant Authentication System

-- ========================================
-- DATABASE SETUP
-- ========================================

-- Create database (if not exists)
CREATE DATABASE IF NOT EXISTS rentalBusiness;
USE rentalBusiness;

-- ========================================
-- VIEWING DATA
-- ========================================

-- View all tenants
SELECT 
    id,
    tenant_identifier,
    name,
    is_active,
    contact_email,
    created_at
FROM tenants
ORDER BY created_at DESC;

-- View all users with their tenant information
SELECT 
    u.id,
    u.email,
    u.first_name,
    u.last_name,
    t.tenant_identifier,
    t.name as tenant_name,
    u.is_active,
    u.email_verified,
    u.account_locked,
    u.failed_login_attempts,
    u.last_login_at,
    u.created_at
FROM users u
JOIN tenants t ON u.tenant_id = t.id
ORDER BY u.created_at DESC;

-- View users for a specific tenant
SELECT 
    u.id,
    u.email,
    u.first_name,
    u.last_name,
    u.is_active,
    u.last_login_at
FROM users u
JOIN tenants t ON u.tenant_id = t.id
WHERE t.tenant_identifier = 'my-company'
ORDER BY u.created_at DESC;

-- View all roles
SELECT 
    id,
    name,
    description,
    is_tenant_specific
FROM roles
ORDER BY name;

-- View all permissions
SELECT 
    id,
    name,
    description,
    resource,
    action
FROM permissions
ORDER BY resource, action;

-- View users with their roles
SELECT 
    u.email,
    u.first_name,
    u.last_name,
    t.tenant_identifier,
    GROUP_CONCAT(r.name) as roles
FROM users u
JOIN tenants t ON u.tenant_id = t.id
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
GROUP BY u.id, u.email, u.first_name, u.last_name, t.tenant_identifier
ORDER BY u.created_at DESC;

-- View roles with their permissions
SELECT 
    r.name as role_name,
    GROUP_CONCAT(p.name) as permissions
FROM roles r
LEFT JOIN role_permissions rp ON r.id = rp.role_id
LEFT JOIN permissions p ON rp.permission_id = p.id
GROUP BY r.id, r.name
ORDER BY r.name;

-- View detailed user information
SELECT 
    u.id,
    u.email,
    u.first_name,
    u.last_name,
    u.phone_number,
    t.tenant_identifier,
    t.name as tenant_name,
    u.is_active,
    u.email_verified,
    u.account_locked,
    u.failed_login_attempts,
    u.last_login_at,
    u.created_at,
    GROUP_CONCAT(DISTINCT r.name) as roles
FROM users u
JOIN tenants t ON u.tenant_id = t.id
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
WHERE u.email = 'admin@mycompany.com' 
  AND t.tenant_identifier = 'my-company'
GROUP BY u.id;

-- ========================================
-- STATISTICS & COUNTS
-- ========================================

-- Count users per tenant
SELECT 
    t.tenant_identifier,
    t.name,
    COUNT(u.id) as user_count,
    SUM(CASE WHEN u.is_active = 1 THEN 1 ELSE 0 END) as active_users,
    SUM(CASE WHEN u.account_locked = 1 THEN 1 ELSE 0 END) as locked_accounts
FROM tenants t
LEFT JOIN users u ON t.id = u.tenant_id
GROUP BY t.id, t.tenant_identifier, t.name
ORDER BY user_count DESC;

-- Count users per role
SELECT 
    r.name as role_name,
    COUNT(ur.user_id) as user_count
FROM roles r
LEFT JOIN user_roles ur ON r.id = ur.role_id
GROUP BY r.id, r.name
ORDER BY user_count DESC;

-- Recent logins
SELECT 
    u.email,
    u.first_name,
    u.last_name,
    t.tenant_identifier,
    u.last_login_at
FROM users u
JOIN tenants t ON u.tenant_id = t.id
WHERE u.last_login_at IS NOT NULL
ORDER BY u.last_login_at DESC
LIMIT 10;

-- ========================================
-- MANAGEMENT QUERIES
-- ========================================

-- Unlock a user account
UPDATE users 
SET account_locked = 0, failed_login_attempts = 0
WHERE email = 'user@example.com' 
  AND tenant_id = (SELECT id FROM tenants WHERE tenant_identifier = 'my-company');

-- Activate a user
UPDATE users 
SET is_active = 1
WHERE email = 'user@example.com' 
  AND tenant_id = (SELECT id FROM tenants WHERE tenant_identifier = 'my-company');

-- Deactivate a user
UPDATE users 
SET is_active = 0
WHERE email = 'user@example.com' 
  AND tenant_id = (SELECT id FROM tenants WHERE tenant_identifier = 'my-company');

-- Verify user email
UPDATE users 
SET email_verified = 1
WHERE email = 'user@example.com' 
  AND tenant_id = (SELECT id FROM tenants WHERE tenant_identifier = 'my-company');

-- Reset failed login attempts
UPDATE users 
SET failed_login_attempts = 0, account_locked = 0
WHERE failed_login_attempts > 0;

-- Add role to user (example: make user a MANAGER)
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'user@example.com'
  AND u.tenant_id = (SELECT id FROM tenants WHERE tenant_identifier = 'my-company')
  AND r.name = 'MANAGER'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    WHERE ur.user_id = u.id AND ur.role_id = r.id
  );

-- Remove role from user
DELETE ur FROM user_roles ur
JOIN users u ON ur.user_id = u.id
JOIN roles r ON ur.role_id = r.id
JOIN tenants t ON u.tenant_id = t.id
WHERE u.email = 'user@example.com'
  AND t.tenant_identifier = 'my-company'
  AND r.name = 'MANAGER';

-- ========================================
-- SECURITY AUDITING
-- ========================================

-- Find locked accounts
SELECT 
    u.email,
    u.first_name,
    u.last_name,
    t.tenant_identifier,
    u.failed_login_attempts,
    u.last_login_at
FROM users u
JOIN tenants t ON u.tenant_id = t.id
WHERE u.account_locked = 1
ORDER BY u.last_login_at DESC;

-- Find users with failed login attempts
SELECT 
    u.email,
    u.first_name,
    u.last_name,
    t.tenant_identifier,
    u.failed_login_attempts,
    u.last_login_at
FROM users u
JOIN tenants t ON u.tenant_id = t.id
WHERE u.failed_login_attempts > 0
ORDER BY u.failed_login_attempts DESC;

-- Find inactive tenants
SELECT 
    tenant_identifier,
    name,
    is_active,
    created_at,
    updated_at
FROM tenants
WHERE is_active = 0;

-- Find users without email verification
SELECT 
    u.email,
    u.first_name,
    u.last_name,
    t.tenant_identifier,
    u.created_at
FROM users u
JOIN tenants t ON u.tenant_id = t.id
WHERE u.email_verified = 0
ORDER BY u.created_at DESC;

-- ========================================
-- CLEANUP QUERIES (USE WITH CAUTION!)
-- ========================================

-- Delete a specific user (and their role associations)
-- DELETE FROM users 
-- WHERE email = 'user@example.com' 
--   AND tenant_id = (SELECT id FROM tenants WHERE tenant_identifier = 'my-company');

-- Delete a tenant and all associated users (CASCADE will handle relations)
-- DELETE FROM tenants 
-- WHERE tenant_identifier = 'tenant-to-delete';

-- ========================================
-- TESTING & DEVELOPMENT
-- ========================================

-- Check if user exists in tenant
SELECT EXISTS(
    SELECT 1 
    FROM users u
    JOIN tenants t ON u.tenant_id = t.id
    WHERE u.email = 'admin@mycompany.com'
      AND t.tenant_identifier = 'my-company'
) as user_exists;

-- Check if tenant exists
SELECT EXISTS(
    SELECT 1 
    FROM tenants
    WHERE tenant_identifier = 'my-company'
) as tenant_exists;

-- Get user's full permission list
SELECT DISTINCT p.name as permission
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
JOIN role_permissions rp ON r.id = rp.role_id
JOIN permissions p ON rp.permission_id = p.id
JOIN tenants t ON u.tenant_id = t.id
WHERE u.email = 'admin@mycompany.com'
  AND t.tenant_identifier = 'my-company'
ORDER BY p.name;

-- ========================================
-- BACKUP & RESTORE (Optional)
-- ========================================

-- Export all data (run in terminal)
-- mysqldump -u root -p rentalBusiness > backup.sql

-- Import data (run in terminal)
-- mysql -u root -p rentalBusiness < backup.sql

-- ========================================
-- NOTES
-- ========================================

/*
1. Password hashes in the database are BCrypt encrypted and cannot be reversed
2. To reset a user's password, you need to update it through the application API
3. Always include tenant_identifier when querying users to ensure proper isolation
4. The system enforces tenant isolation at the application level, so direct DB queries 
   may show data from multiple tenants
5. Be careful with DELETE operations - use soft deletes (is_active=0) where possible
6. Always backup before running UPDATE or DELETE queries in production
*/

