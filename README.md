# ServiceForEvent - Event Planning and Service Provider Platform

## Overview
ServiceForEvent is a comprehensive microservices-based platform that connects event organizers with service providers. The platform facilitates event planning, service provider booking, and event management through a set of interconnected services.

## System Architecture

### Microservices Architecture
The application is built using a microservices architecture with four core services:

1. **User Service** (Port: 8081)
   - Handles user authentication and profile management
   - Manages user roles (USER, PROVIDER, ADMIN)
   - Endpoints:
     ```http
     POST   /api/users/register     - Register new user
     GET    /api/users/{id}        - Get user profile
     PUT    /api/users/{id}        - Update user profile
     GET    /api/users/me          - Get current user
     DELETE /api/users/all         - Delete all users (testing)
     ```

2. **Provider Service** (Port: 8083)
   - Manages service provider profiles
   - Handles provider verification
   - Manages availability slots
   - Rating system
   - Endpoints:
     ```http
     POST   /api/providers/register              - Register new provider
     GET    /api/providers/{id}                 - Get provider details
     PUT    /api/providers/{id}                 - Update provider
     PUT    /api/providers/{id}/verify          - Verify provider
     POST   /api/providers/{id}/availability    - Add availability slots
     POST   /api/providers/{id}/rating          - Add provider rating
     GET    /api/providers/service-type/{type}  - Search by service type
     GET    /api/providers/verified             - Get verified providers
     PUT    /api/providers/{id}/activate        - Activate/deactivate provider
     DELETE /api/providers/all                  - Delete all providers (testing)
     ```

3. **Event Service** (Port: 8082)
   - Handles event creation and management
   - Manages event types and requirements
   - Endpoints:
     ```http
     POST   /api/events                    - Create new event
     GET    /api/events/{id}              - Get event details
     GET    /api/events/user              - Get user's events
     GET    /api/events/type/{eventType}  - Search by event type
     PUT    /api/events/{id}              - Update event
     DELETE /api/events/{id}              - Delete event
     DELETE /api/events/all               - Delete all events (testing)
     ```

4. **Booking Service** (Port: 8084)
   - Manages booking workflow
   - Handles payment status
   - Booking lifecycle management
   - Endpoints:
     ```http
     POST   /api/bookings                          - Create booking
     GET    /api/bookings/{id}                     - Get booking details
     GET    /api/bookings/user                     - Get user's bookings
     GET    /api/bookings/provider/{providerId}    - Get provider's bookings
     GET    /api/bookings/event/{eventId}         - Get event's bookings
     PUT    /api/bookings/{id}/confirm            - Confirm booking
     PUT    /api/bookings/{id}/cancel             - Cancel booking
     PUT    /api/bookings/{id}/complete           - Complete booking
     PUT    /api/bookings/{id}/reject             - Reject booking
     PUT    /api/bookings/{id}/payment            - Update payment status
     GET    /api/bookings/provider/{id}/availability - Check availability
     DELETE /api/bookings/all                      - Delete all bookings (testing)
     ```

### Data Models

1. **User**
   ```json
   {
     "id": "Long",
     "firstName": "String",
     "lastName": "String",
     "email": "String",
     "password": "String (hashed)",
     "phoneNumber": "String",
     "address": "String",
     "enabled": "boolean",
     "roles": ["String"]
   }
   ```

2. **Provider**
   ```json
   {
     "id": "Long",
     "businessName": "String",
     "ownerName": "String",
     "email": "String",
     "password": "String (hashed)",
     "phoneNumber": "String",
     "address": "String",
     "description": "String",
     "serviceTypes": ["String"],
     "basePrice": "BigDecimal",
     "verified": "boolean",
     "active": "boolean",
     "availabilitySlots": ["AvailabilitySlot"],
     "rating": "double",
     "totalReviews": "int"
   }
   ```

3. **Event**
   ```json
   {
     "id": "Long",
     "name": "String",
     "description": "String",
     "eventDate": "LocalDateTime",
     "eventType": "String",
     "location": "String",
     "expectedGuests": "int",
     "budget": "BigDecimal",
     "userId": "Long",
     "status": "String",
     "requiredServiceIds": ["Long"]
   }
   ```

4. **Booking**
   ```json
   {
     "id": "Long",
     "userId": "Long",
     "providerId": "Long",
     "eventId": "Long",
     "serviceDate": "LocalDateTime",
     "serviceType": "String",
     "amount": "BigDecimal",
     "status": "String",
     "notes": "String",
     "paid": "boolean"
   }
   ```

### Error Handling

All endpoints return standardized error responses:
```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

Common HTTP Status Codes:
- 200: Success
- 400: Bad Request (invalid input)
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error

### Required Headers
- `Content-Type: application/json`
- `X-User-ID`: Required for authenticated endpoints

### Testing

1. **Prerequisites**
   - All services must be running
   - H2 Console available at:
     - User Service: http://localhost:8081/h2-console
     - Event Service: http://localhost:8082/h2-console
     - Provider Service: http://localhost:8083/h2-console
     - Booking Service: http://localhost:8084/h2-console

2. **Test Sequence**
   ```bash
   # 1. Clean all databases
   DELETE /api/bookings/all
   DELETE /api/events/all
   DELETE /api/providers/all
   DELETE /api/users/all

   # 2. Register user
   # 3. Register provider
   # 4. Add provider availability
   # 5. Create event
   # 6. Create and manage booking
   ```

3. **Complete test collection available in:**
   ```
   /http-tests.http
   ```

### Security

1. **Authentication**
   - Basic authentication implemented
   - Password encryption using BCrypt
   - JWT support planned

2. **Authorization**
   - Role-based access control
   - Supported roles: USER, PROVIDER, ADMIN

### Database Configuration

H2 Database settings (per service):
```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/serviceDB
    username: sa
    password: password
  h2:
    console:
      enabled: true
      path: /h2-console
```

### Future Enhancements

1. **Technical Improvements**
   - Service discovery
   - API Gateway
   - Distributed logging
   - Message queue
   - Production database

2. **Feature Enhancements**
   - Real-time notifications
   - Advanced search
   - Payment gateway
   - Calendar integration
   - Mobile app
   - Chat system

## Setup and Deployment

1. **Prerequisites**
   ```bash
   Java 11+
   Maven 3.6+
   Available ports: 8081-8084
   ```

2. **Build and Run**
   ```bash
   # Clone and build
   git clone [repository-url]
   cd ServiceForEvent
   mvn clean install

   # Start services in order
   cd user-service && mvn spring-boot:run
   cd ../provider-service && mvn spring-boot:run
   cd ../event-service && mvn spring-boot:run
   cd ../booking-service && mvn spring-boot:run
   ```

## Support and Documentation

- API Documentation: Swagger UI (planned)
- Test Collection: http-tests.http
- Database Console: H2 Console
- Source Code: Inline documentation 

## Production Readiness Requirements

### Security Enhancements
1. **Authentication & Authorization**
   - Implement JWT-based authentication
   - Add OAuth2 support for social login
   - Implement API key management for B2B integrations
   - Add rate limiting and request throttling
   - Implement IP whitelisting for admin endpoints

2. **Infrastructure Security**
   - Set up WAF (Web Application Firewall)
   - Implement DDoS protection
   - Add SSL/TLS encryption for all services
   - Implement secrets management (e.g., HashiCorp Vault)
   - Regular security audits and penetration testing

### Scalability & Performance
1. **Service Discovery & Load Balancing**
   - Implement Eureka/Consul for service discovery
   - Set up load balancing with Spring Cloud LoadBalancer
   - Add circuit breakers with Resilience4j
   - Implement API Gateway (Spring Cloud Gateway)

2. **Database Optimization**
   - Migrate from H2 to production database (PostgreSQL/MySQL)
   - Implement database clustering
   - Add connection pooling (HikariCP)
   - Set up database replication
   - Implement caching strategy (Redis)

3. **Monitoring & Observability**
   - Set up centralized logging (ELK Stack)
   - Implement distributed tracing (Zipkin/Jaeger)
   - Add metrics collection (Prometheus)
   - Set up monitoring dashboards (Grafana)
   - Implement health checks and alerting

4. **DevOps Requirements**
   - Containerization (Docker)
   - Container orchestration (Kubernetes)
   - CI/CD pipeline setup
   - Automated testing (Unit, Integration, E2E)
   - Infrastructure as Code (Terraform)
   - Backup and disaster recovery plan

### Data Management
1. **Data Retention & Compliance**
   - Implement data retention policies
   - Add GDPR compliance features
   - Set up data backup and recovery
   - Implement audit logging
   - Add data encryption at rest

2. **Performance Optimization**
   - Implement database indexing strategy
   - Add query optimization
   - Set up data archival process
   - Implement data partitioning

## Extended Future Services

### Core Service Extensions
1. **Notification Service** (Port: 8085)
   - Real-time event updates
   - Email notifications
   - SMS notifications
   - Push notifications
   - Webhook integration

2. **Payment Service** (Port: 8086)
   - Multiple payment gateway integration
   - Subscription management
   - Refund processing
   - Invoice generation
   - Payment analytics

3. **Analytics Service** (Port: 8087)
   - Business intelligence
   - User behavior analytics
   - Revenue analytics
   - Provider performance metrics
   - Custom report generation

4. **Review & Rating Service** (Port: 8088)
   - Provider reviews
   - Event feedback
   - Rating aggregation
   - Sentiment analysis
   - Review moderation

### Additional Services
1. **Chat Service** (Port: 8089)
   - Real-time messaging
   - File sharing
   - Group chat for events
   - Chat history
   - Message encryption

2. **Calendar Service** (Port: 8090)
   - Event scheduling
   - Availability management
   - Calendar sync (Google, Outlook)
   - Reminder system
   - Timezone management

3. **Document Service** (Port: 8091)
   - Contract management
   - Document templates
   - E-signatures
   - Document versioning
   - File storage

4. **Vendor Management Service** (Port: 8092)
   - Vendor onboarding
   - Vendor verification
   - Service catalog management
   - Vendor analytics
   - Quality scoring

5. **Inventory Service** (Port: 8093)
   - Equipment tracking
   - Resource allocation
   - Inventory analytics
   - Maintenance scheduling
   - Asset management

6. **Support Service** (Port: 8094)
   - Ticket management
   - Knowledge base
   - FAQ management
   - Customer support chat
   - Issue tracking

7. **Marketing Service** (Port: 8095)
   - Campaign management
   - Email marketing
   - Promotion management
   - Referral system
   - SEO optimization

### Mobile Applications
1. **Customer Mobile App**
   - Event browsing and booking
   - Real-time notifications
   - Payment management
   - Chat with providers
   - Event tracking

2. **Provider Mobile App**
   - Booking management
   - Schedule management
   - Real-time updates
   - Payment tracking
   - Service management 

## Running with IntelliJ IDEA

### Initial Setup
1. Open IntelliJ IDEA
2. Go to `File` → `Open` and select the `ServiceForEvent` directory
3. Wait for IntelliJ to import the Maven project and download dependencies
4. Ensure you have JDK 11 or higher configured in IntelliJ

### Compiling the Project
1. Open the Maven tool window (`View` → `Tool Windows` → `Maven`)
2. Expand each service module
3. Run the following Maven goals for each service:
   - `clean`: Cleans the target directory
   - `compile`: Compiles the source code
   - `install`: Installs the package in local Maven repository

Or use the Maven command line:
```bash
mvn clean install -DskipTests
```

### Running the Services
1. Open each service's main application class:
   - User Service: `UserServiceApplication.java`
   - Provider Service: `ProviderServiceApplication.java`
   - Event Service: `EventServiceApplication.java`
   - Booking Service: `BookingServiceApplication.java`

2. Click the green play button next to the `main` method or use the shortcut `Shift + F10`

3. Verify each service is running by checking:
   - User Service: http://localhost:8081/actuator/health
   - Provider Service: http://localhost:8083/actuator/health
   - Event Service: http://localhost:8082/actuator/health
   - Booking Service: http://localhost:8084/actuator/health

### Running Tests
1. **Unit Tests**
   - Right-click on the `src/test/java` folder in each service
   - Select `Run 'All Tests'`
   - Or use the Maven test goal: `mvn test`

2. **Integration Tests using HTTP Requests**
   - Open `http-tests.http` file in IntelliJ
   - You'll see green "Run" buttons next to each request
   - Click the "Run All Requests" button at the top to run all tests in sequence
   - Or run individual requests by clicking the green play button next to each one
   - View responses in the "Response" tab below each request

3. **Monitoring Test Results**
   - Use the H2 Console to verify database changes:
     - User Service: http://localhost:8081/h2-console
     - Provider Service: http://localhost:8083/h2-console
     - Event Service: http://localhost:8082/h2-console
     - Booking Service: http://localhost:8084/h2-console
   - Check the IntelliJ console for detailed logs
   - Use the "Services" tool window to monitor all running services

### Debugging
1. Set breakpoints by clicking in the left gutter of the editor
2. Right-click on the application class and select "Debug"
3. Use the Debug tool window to:
   - Step through code
   - Inspect variables
   - Evaluate expressions
   - View call stack

### Common Issues and Solutions
1. **Port Already in Use**
   - Check if another process is using the required ports (8081-8084)
   - Kill the process or change the port in `application.properties`

2. **Database Errors**
   - Clear the H2 database files in the `data` directory
   - Restart the services

3. **Build Failures**
   - Run `mvn clean install -U` to force update dependencies
   - Check Maven settings and JDK configuration

4. **Test Failures**
   - Ensure all services are running
   - Clear databases using the cleanup endpoints
   - Check the sequence of test requests 

## GitHub Setup and Upload

### First-Time GitHub Setup
1. **Install Git**
   - Download Git from https://git-scm.com/downloads
   - Install with default settings
   - Verify installation by opening terminal/command prompt and typing:
     ```bash
     git --version
     ```

2. **Configure Git in IntelliJ**
   - Open IntelliJ IDEA
   - Go to `File` → `Settings` (Windows) or `IntelliJ IDEA` → `Preferences` (Mac)
   - Navigate to `Version Control` → `Git`
   - Click `Test` to verify Git installation

3. **Configure GitHub Account**
   - In the same Settings window, go to `Version Control` → `GitHub`
   - Click `+` to add your GitHub account
   - Choose either:
     - `Log In via GitHub`: Easier, opens browser for authentication
     - `Log In with Token`: Generate token from GitHub settings

### Creating GitHub Repository
1. **Create New Repository on GitHub**
   - Go to https://github.com
   - Click the `+` button in the top-right corner
   - Select `New repository`
   - Enter repository name: `ServiceForEvent`
   - Leave it public or make it private
   - DO NOT initialize with README (we already have one)
   - Click `Create repository`

### Uploading Project from IntelliJ
1. **Initialize Git Repository**
   - In IntelliJ, go to `VCS` → `Enable Version Control Integration`
   - Select `Git` from the dropdown
   - Click `OK`

2. **Add Files to Git**
   - In the Project window, right-click the project root
   - Go to `Git` → `Add`
   - This stages all files for commit

3. **Create .gitignore File**
   - Right-click project root → `New` → `File`
   - Name it `.gitignore`
   - Add these common entries:
     ```
     HELP.md
     target/
     !.mvn/wrapper/maven-wrapper.jar
     !**/src/main/**/target/
     !**/src/test/**/target/
     
     ### STS ###
     .apt_generated
     .classpath
     .factorypath
     .project
     .settings
     .springBeans
     .sts4-cache
     
     ### IntelliJ IDEA ###
     .idea
     *.iws
     *.iml
     *.ipr
     
     ### VS Code ###
     .vscode/
     
     ### H2 Database ###
     *.db
     data/
     ```

4. **Commit Files**
   - Press `Ctrl + K` (Windows) or `Cmd + K` (Mac)
   - Enter a commit message: "Initial commit"
   - Click `Commit`

5. **Add Remote Repository**
   - Copy the HTTPS URL from your GitHub repository
   - In IntelliJ, go to `Git` → `Manage Remotes`
   - Click `+` and paste the URL
   - Click `OK`

6. **Push to GitHub**
   - Go to `Git` → `Push` (or `Ctrl + Shift + K`)
   - Click `Push`

### Updating Your Repository
1. **Making Changes**
   - Edit your files as normal
   - Changes will appear in red in the Project window

2. **Committing Changes**
   - Press `Ctrl + K`
   - Enter a descriptive commit message
   - Click `Commit`

3. **Pushing Changes**
   - Press `Ctrl + Shift + K`
   - Click `Push`

### Common Git Commands (if needed)
```bash
# Check status
git status

# Add files
git add .

# Commit changes
git commit -m "Your message"

# Push to GitHub
git push origin main

# Pull latest changes
git pull origin main
``` 