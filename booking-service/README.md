# Booking Service

Manages service bookings and scheduling between users and service providers.

## API Documentation

### Create Booking

```http
POST /api/bookings
Header: X-User-ID: {userId}
```

Request Body:
```json
{
    "providerId": 1,
    "eventId": 1,
    "serviceDate": "2024-04-10T14:00:00",
    "serviceType": "CATERING",
    "amount": 1500.00,
    "notes": "Special dietary requirements: vegetarian options needed"
}
```

Response:
```json
{
    "success": true,
    "message": "Booking created successfully",
    "data": {
        "id": 1,
        "userId": 1,
        "providerId": 1,
        "eventId": 1,
        "bookingDate": "2024-04-01T10:30:00",
        "serviceDate": "2024-04-10T14:00:00",
        "serviceType": "CATERING",
        "amount": 1500.00,
        "status": "PENDING",
        "notes": "Special dietary requirements: vegetarian options needed",
        "isPaid": false
    }
}
```

### Get Booking by ID

```http
GET /api/bookings/{id}
```

Response:
```json
{
    "success": true,
    "data": {
        "id": 1,
        "userId": 1,
        "providerId": 1,
        "eventId": 1,
        "bookingDate": "2024-04-01T10:30:00",
        "serviceDate": "2024-04-10T14:00:00",
        "serviceType": "CATERING",
        "amount": 1500.00,
        "status": "PENDING",
        "notes": "Special dietary requirements: vegetarian options needed",
        "isPaid": false
    }
}
```

### Get User's Bookings

```http
GET /api/bookings/user
Header: X-User-ID: {userId}
```

Response:
```json
{
    "success": true,
    "data": [
        {
            "id": 1,
            "userId": 1,
            "providerId": 1,
            "eventId": 1,
            "bookingDate": "2024-04-01T10:30:00",
            "serviceDate": "2024-04-10T14:00:00",
            "serviceType": "CATERING",
            "amount": 1500.00,
            "status": "PENDING",
            "notes": "Special dietary requirements: vegetarian options needed",
            "isPaid": false
        }
    ]
}
```

### Get Provider's Bookings

```http
GET /api/bookings/provider/{providerId}
```

### Get Event's Bookings

```http
GET /api/bookings/event/{eventId}
```

### Get Provider's Bookings by Status

```http
GET /api/bookings/provider/{providerId}/status/{status}
```

Status values: PENDING, CONFIRMED, CANCELLED, COMPLETED, REJECTED

### Get User's Bookings by Status

```http
GET /api/bookings/user/status/{status}
Header: X-User-ID: {userId}
```

### Confirm Booking

```http
PUT /api/bookings/{id}/confirm
```

Response:
```json
{
    "success": true,
    "message": "Booking confirmed successfully",
    "data": {
        "id": 1,
        "status": "CONFIRMED"
    }
}
```

### Cancel Booking

```http
PUT /api/bookings/{id}/cancel?reason={reason}
```

### Complete Booking

```http
PUT /api/bookings/{id}/complete
```

### Reject Booking

```http
PUT /api/bookings/{id}/reject?reason={reason}
```

### Update Payment Status

```http
PUT /api/bookings/{id}/payment?isPaid=true
```

### Check Provider Availability

```http
GET /api/bookings/provider/{providerId}/availability?serviceDate=2024-04-10T14:00:00
```

Response:
```json
{
    "success": true,
    "data": true
}
```

## Error Responses

All endpoints may return the following error responses:

### Not Found (404)
```json
{
    "success": false,
    "message": "Booking not found"
}
```

### Bad Request (400)
```json
{
    "success": false,
    "message": "Provider is not available at the requested time"
}
```

### Validation Error (400)
```json
{
    "success": false,
    "message": "Validation failed",
    "errors": [
        "Service date is required",
        "Amount is required"
    ]
}
```

## Database Schema

### Booking Table
```sql
CREATE TABLE bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    booking_date TIMESTAMP NOT NULL,
    service_date TIMESTAMP NOT NULL,
    service_type VARCHAR(255) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    notes TEXT,
    cancellation_reason VARCHAR(255),
    is_paid BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT
);
```

## Running in Development

1. Start the service:
   ```bash
   mvn spring-boot:run
   ```

2. Access H2 Console:
   - URL: http://localhost:8084/h2-console
   - JDBC URL: jdbc:h2:mem:bookingdb
   - Username: sa
   - Password: password

3. Test the API:
   ```bash
   curl -X POST http://localhost:8084/api/bookings \
   -H "Content-Type: application/json" \
   -H "X-User-ID: 1" \
   -d '{
       "providerId": 1,
       "eventId": 1,
       "serviceDate": "2024-04-10T14:00:00",
       "serviceType": "CATERING",
       "amount": 1500.00,
       "notes": "Special dietary requirements"
   }'
   ``` 