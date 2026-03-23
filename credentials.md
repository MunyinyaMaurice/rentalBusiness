# Credentials

## Super Admin (Business Management)

Use this account to:
- check registered businesses
- activate a business
- deactivate a business

These values are bootstrapped from `application.properties`.

- Email: `superadmin@rentalbusiness.local`
- Password: `SuperAdmin@123`
- Tenant/Business Identifier (for login): `SYSTEM`

Login API:
- `POST /api/auth/login`

Business APIs:
- `GET /api/businesses`
- `GET /api/businesses/{businessIdentifier}`
- `PATCH /api/businesses/{businessIdentifier}/activate`
- `PATCH /api/businesses/{businessIdentifier}/deactivate`

Compatibility endpoints (still supported):
- `GET /api/tenants`
- `GET /api/tenants/{businessIdentifier}`
- `PATCH /api/tenants/{businessIdentifier}/activate`
- `PATCH /api/tenants/{businessIdentifier}/deactivate`

## Postman / 403 on `GET /api/businesses`

1. Call **login** first and copy the **`token`** from the JSON response (not the whole object).
2. On **List Businesses**, open **Authorization** → **Bearer Token** and paste that token, **or** set the parent folder to inherit auth and save the token in a collection variable.
3. Add header **`Authorization`**: `Bearer <paste-token-here>` (no quotes, no line breaks).

If the server logs show **`Securing GET /error`** with **anonymous**, that is usually the **error page** for a failed request; the real problem is missing/invalid JWT on **`GET /api/businesses`**. Check logs for: *JWT present but validation failed*.
