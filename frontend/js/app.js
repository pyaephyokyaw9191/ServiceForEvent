document.addEventListener('DOMContentLoaded', () => {
    // Page Navigation
    const pages = document.querySelectorAll('.page');
    const navLinks = document.querySelectorAll('[data-page]');

    function showPage(pageId) {
        pages.forEach(page => page.classList.remove('active'));
        document.getElementById(pageId + 'Page').classList.add('active');
    }

    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            showPage(e.target.dataset.page);
        });
    });

    // Authentication
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const logoutBtn = document.getElementById('logoutBtn');

    function updateAuthUI(isLoggedIn) {
        document.getElementById('loginNav').classList.toggle('d-none', isLoggedIn);
        document.getElementById('registerNav').classList.toggle('d-none', isLoggedIn);
        document.getElementById('profileNav').classList.toggle('d-none', !isLoggedIn);
        document.getElementById('logoutNav').classList.toggle('d-none', !isLoggedIn);
    }

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        try {
            const response = await api.login(
                document.getElementById('loginEmail').value,
                document.getElementById('loginPassword').value
            );
            console.log('Login response:', response); // Debug log
            updateAuthUI(true);
            showPage('home');
            await loadUserData();
        } catch (error) {
            console.error('Login error:', error); // Debug log
            alert(error.message || 'Login failed. Please try again.');
        }
    });

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        try {
            const userData = {
                firstName: document.getElementById('registerFirstName').value,
                lastName: document.getElementById('registerLastName').value,
                email: document.getElementById('registerEmail').value,
                password: document.getElementById('registerPassword').value,
                phoneNumber: document.getElementById('registerPhone').value
            };
            await api.register(userData);
            alert('Registration successful! Please login.');
            showPage('login');
        } catch (error) {
            alert(error.message);
        }
    });

    logoutBtn.addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.removeItem(CONFIG.TOKEN_KEY);
        localStorage.removeItem(CONFIG.USER_KEY);
        updateAuthUI(false);
        showPage('home');
    });

    // Events Management
    const createEventBtn = document.getElementById('createEventBtn');
    const eventsList = document.getElementById('eventsList');

    async function loadEvents() {
        try {
            const events = await api.getUserEvents();
            eventsList.innerHTML = events.map(event => `
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">${event.name}</h5>
                            <p class="card-text">${event.description}</p>
                            <p class="card-text">
                                <small class="text-muted">
                                    Date: ${new Date(event.eventDate).toLocaleDateString()}
                                </small>
                            </p>
                            <button class="btn btn-primary" onclick="viewEventDetails(${event.id})">
                                View Details
                            </button>
                        </div>
                    </div>
                </div>
            `).join('');
        } catch (error) {
            alert(error.message);
        }
    }

    // Providers Management
    const providersList = document.getElementById('providersList');
    const serviceTypeFilter = document.getElementById('serviceTypeFilter');
    const providerSearch = document.getElementById('providerSearch');

    async function loadProviders() {
        try {
            const providers = await api.getProviders();
            displayProviders(providers);
        } catch (error) {
            alert(error.message);
        }
    }

    function displayProviders(providers) {
        providersList.innerHTML = providers.map(provider => `
            <div class="col-md-4">
                <div class="card provider-card" onclick="viewProviderDetails(${provider.id})">
                    <div class="card-body">
                        <h5 class="card-title">${provider.businessName}</h5>
                        <p class="card-text">${provider.description}</p>
                        <p class="card-text">
                            <small class="text-muted">
                                Services: ${provider.serviceTypes.join(', ')}
                            </small>
                        </p>
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="badge ${provider.verified ? 'bg-success' : 'bg-warning'}">
                                ${provider.verified ? 'Verified' : 'Pending'}
                            </span>
                            <span class="text-primary">
                                Rating: ${provider.rating.toFixed(1)} (${provider.totalReviews})
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        `).join('');
    }

    serviceTypeFilter.addEventListener('change', async () => {
        try {
            const serviceType = serviceTypeFilter.value;
            const providers = serviceType ? 
                await api.getProvidersByService(serviceType) : 
                await api.getProviders();
            displayProviders(providers);
        } catch (error) {
            alert(error.message);
        }
    });

    // Bookings Management
    const bookingsList = document.getElementById('bookingsList');

    async function loadBookings() {
        try {
            const bookings = await api.getUserBookings();
            bookingsList.innerHTML = bookings.map(booking => `
                <div class="card booking-card">
                    <div class="card-body">
                        <h5 class="card-title">Booking #${booking.id}</h5>
                        <p class="card-text">
                            Service: ${booking.serviceType}<br>
                            Date: ${new Date(booking.serviceDate).toLocaleDateString()}<br>
                            Amount: $${booking.amount}
                        </p>
                        <div class="booking-status status-${booking.status.toLowerCase()}">
                            Status: ${booking.status}
                        </div>
                        ${getBookingActions(booking)}
                    </div>
                </div>
            `).join('');
        } catch (error) {
            alert(error.message);
        }
    }

    function getBookingActions(booking) {
        if (booking.status === 'PENDING') {
            return `
                <button class="btn btn-success btn-sm" onclick="updateBookingStatus(${booking.id}, 'confirm')">
                    Confirm
                </button>
                <button class="btn btn-danger btn-sm" onclick="updateBookingStatus(${booking.id}, 'cancel')">
                    Cancel
                </button>
            `;
        }
        if (booking.status === 'CONFIRMED' && !booking.paid) {
            return `
                <button class="btn btn-primary btn-sm" onclick="processPayment(${booking.id})">
                    Pay Now
                </button>
            `;
        }
        return '';
    }

    // Initialize
    function init() {
        const token = localStorage.getItem(CONFIG.TOKEN_KEY);
        updateAuthUI(!!token);
        if (token) {
            loadUserData();
        }
    }

    async function loadUserData() {
        try {
            console.log('Loading user data...'); // Debug log
            const userProfile = await api.getUserProfile();
            console.log('User profile:', userProfile); // Debug log
            
            // Only load events and bookings if profile is loaded
            if (userProfile) {
                await Promise.all([
                    loadEvents(),
                    loadBookings()
                ]);
            }
        } catch (error) {
            console.error('Failed to load user data:', error);
            if (error.message.includes('401')) {
                // If unauthorized, clear token and show login
                localStorage.removeItem(CONFIG.TOKEN_KEY);
                localStorage.removeItem(CONFIG.USER_KEY);
                updateAuthUI(false);
                showPage('login');
                alert('Session expired. Please login again.');
            }
        }
    }

    // Global functions
    window.viewEventDetails = async (eventId) => {
        // Implement event details view
    };

    window.viewProviderDetails = async (providerId) => {
        // Implement provider details view
    };

    window.updateBookingStatus = async (bookingId, status) => {
        try {
            await api.updateBookingStatus(bookingId, status);
            loadBookings();
        } catch (error) {
            alert(error.message);
        }
    };

    window.processPayment = async (bookingId) => {
        try {
            await api.updatePaymentStatus(bookingId, { paid: true });
            loadBookings();
        } catch (error) {
            alert(error.message);
        }
    };

    // Load initial data
    init();
    loadProviders();
}); 