const CONFIG = {
    API: {
        USER_SERVICE: 'http://localhost:8081/api',
        EVENT_SERVICE: 'http://localhost:8082/api',
        PROVIDER_SERVICE: 'http://localhost:8083/api',
        BOOKING_SERVICE: 'http://localhost:8084/api'
    },
    ENDPOINTS: {
        // User Service
        LOGIN: '/users/login',
        REGISTER: '/users/register',
        USER_PROFILE: '/users/me',
        
        // Event Service
        EVENTS: '/events',
        EVENT_BY_ID: '/events/',
        USER_EVENTS: '/events/user',
        
        // Provider Service
        PROVIDERS: '/providers',
        PROVIDER_BY_ID: '/providers/',
        PROVIDER_BY_SERVICE: '/providers/service-type/',
        PROVIDER_VERIFY: '/providers/verify/',
        PROVIDER_AVAILABILITY: '/providers/availability/',
        
        // Booking Service
        BOOKINGS: '/bookings',
        BOOKING_BY_ID: '/bookings/',
        USER_BOOKINGS: '/bookings/user',
        PROVIDER_BOOKINGS: '/bookings/provider/',
        BOOKING_CONFIRM: '/bookings/confirm/',
        BOOKING_CANCEL: '/bookings/cancel/',
        BOOKING_COMPLETE: '/bookings/complete/',
        BOOKING_PAYMENT: '/bookings/payment/'
    },
    TOKEN_KEY: 'serviceforevent_token',
    USER_KEY: 'serviceforevent_user'
}; 