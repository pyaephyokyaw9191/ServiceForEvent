class ApiService {
    constructor() {
        this.token = localStorage.getItem(CONFIG.TOKEN_KEY);
    }

    getHeaders() {
        const headers = {
            'Content-Type': 'application/json'
        };
        
        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }
        
        return headers;
    }

    async handleResponse(response) {
        const contentType = response.headers.get('content-type');
        let data;
        
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            data = await response.text();
            console.warn('Non-JSON response:', data);
            throw new Error('Invalid response format from server');
        }

        if (!response.ok) {
            console.error('API Error:', data);
            throw new Error(data.message || data.error || 'Something went wrong');
        }

        return data;
    }

    // User Service
    async login(email, password) {
        try {
            const response = await fetch(CONFIG.API.USER_SERVICE + CONFIG.ENDPOINTS.LOGIN, {
                method: 'POST',
                headers: this.getHeaders(),
                body: JSON.stringify({ email, password })
            });
            
            const data = await this.handleResponse(response);
            console.log('Login response data:', data); // Debug log

            if (data.token) {
                this.token = data.token;
                localStorage.setItem(CONFIG.TOKEN_KEY, data.token);
                if (data.user) {
                    localStorage.setItem(CONFIG.USER_KEY, JSON.stringify(data.user));
                }
            } else {
                console.error('No token in login response:', data);
                throw new Error('Invalid login response');
            }
            
            return data;
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    }

    async register(userData) {
        const response = await fetch(CONFIG.API.USER_SERVICE + CONFIG.ENDPOINTS.REGISTER, {
            method: 'POST',
            headers: this.getHeaders(),
            body: JSON.stringify(userData)
        });
        return this.handleResponse(response);
    }

    async getUserProfile() {
        const response = await fetch(CONFIG.API.USER_SERVICE + CONFIG.ENDPOINTS.USER_PROFILE, {
            headers: this.getHeaders()
        });
        return this.handleResponse(response);
    }

    // Event Service
    async getEvents() {
        const response = await fetch(CONFIG.API.EVENT_SERVICE + CONFIG.ENDPOINTS.EVENTS, {
            headers: this.getHeaders()
        });
        return this.handleResponse(response);
    }

    async createEvent(eventData) {
        const response = await fetch(CONFIG.API.EVENT_SERVICE + CONFIG.ENDPOINTS.EVENTS, {
            method: 'POST',
            headers: this.getHeaders(),
            body: JSON.stringify(eventData)
        });
        return this.handleResponse(response);
    }

    async getUserEvents() {
        const response = await fetch(CONFIG.API.EVENT_SERVICE + CONFIG.ENDPOINTS.USER_EVENTS, {
            headers: this.getHeaders()
        });
        return this.handleResponse(response);
    }

    // Provider Service
    async getProviders() {
        const response = await fetch(CONFIG.API.PROVIDER_SERVICE + CONFIG.ENDPOINTS.PROVIDERS, {
            headers: this.getHeaders()
        });
        return this.handleResponse(response);
    }

    async getProvidersByService(serviceType) {
        const response = await fetch(CONFIG.API.PROVIDER_SERVICE + CONFIG.ENDPOINTS.PROVIDER_BY_SERVICE + serviceType, {
            headers: this.getHeaders()
        });
        return this.handleResponse(response);
    }

    async getProviderAvailability(providerId) {
        const response = await fetch(CONFIG.API.PROVIDER_SERVICE + CONFIG.ENDPOINTS.PROVIDER_AVAILABILITY + providerId, {
            headers: this.getHeaders()
        });
        return this.handleResponse(response);
    }

    // Booking Service
    async createBooking(bookingData) {
        const response = await fetch(CONFIG.API.BOOKING_SERVICE + CONFIG.ENDPOINTS.BOOKINGS, {
            method: 'POST',
            headers: this.getHeaders(),
            body: JSON.stringify(bookingData)
        });
        return this.handleResponse(response);
    }

    async getUserBookings() {
        const response = await fetch(CONFIG.API.BOOKING_SERVICE + CONFIG.ENDPOINTS.USER_BOOKINGS, {
            headers: this.getHeaders()
        });
        return this.handleResponse(response);
    }

    async updateBookingStatus(bookingId, status) {
        const endpoint = CONFIG.API.BOOKING_SERVICE + CONFIG.ENDPOINTS[`BOOKING_${status.toUpperCase()}`] + bookingId;
        const response = await fetch(endpoint, {
            method: 'PUT',
            headers: this.getHeaders()
        });
        return this.handleResponse(response);
    }

    async updatePaymentStatus(bookingId, paymentData) {
        const response = await fetch(CONFIG.API.BOOKING_SERVICE + CONFIG.ENDPOINTS.BOOKING_PAYMENT + bookingId, {
            method: 'PUT',
            headers: this.getHeaders(),
            body: JSON.stringify(paymentData)
        });
        return this.handleResponse(response);
    }
}

const api = new ApiService(); 